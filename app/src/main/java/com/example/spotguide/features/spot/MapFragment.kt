package com.example.spotguide.features.spot

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.spotguide.R
import com.example.spotguide.core.base.BaseFragment
import com.example.spotguide.core.extension.stringFromRes
import com.example.spotguide.core.navigation.Navigation
import com.example.spotguide.features.spot.logic.SpotEvents
import com.example.spotguide.features.spot.logic.SpotStates
import com.example.spotguide.features.spot.logic.SpotViewModel
import com.example.spotguide.ui.action_bar.ActionBarParams
import com.example.spotguide.ui.action_bar.Text
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import io.uniflow.androidx.flow.onEvents
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.android.ext.android.inject
import java.util.*

class MapFragment : BaseFragment(),
    GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerClickListener {

    override val actionBarParams = ActionBarParams(
        middleText = Text(R.string.app_name.stringFromRes(), R.color.colorAccent)
    )

    override val layoutResId: Int
        get() = R.layout.fragment_map

    private val spotViewModel: SpotViewModel by inject()

    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private val locationManager: LocationManager by lazy { activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private var lastKnowLocation: Location? = null
    private var initialZoom = false

    override fun setViewModelStates() {
        onStates(spotViewModel) { state ->
            when (state) {
                is SpotStates.SpotsLoading -> activity.showLoading()
                is SpotStates.SpotsLoaded -> onSpotsLoaded(state.spots)
            }
        }
    }

    override fun setViewModelEvents() {
        onEvents(spotViewModel) {event ->
            when (val data = event.take()) {
                is SpotEvents.SpotsLoadingFail -> onSpotsLoadingFail()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMaps(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupUI() {

        handlePermissions()
        setupButtons()
        spotViewModel.getSpots()
    }

    // Handling states section START

    private fun onSpotsLoaded(spots: List<Spot>) {
        spots.forEach {
            createMarkerOnPosition(it.location, it)
        }
        activity.hideLoading()
    }

    private fun onSpotsLoadingFail() {
        handleError("Spots loading error.")
        activity.hideLoading()
    }

    // Handling states section END

    private fun setupButtons() {
        bAddSpot.setOnClickListener {
            lastKnowLocation?.let {
                Navigation.switchFragments(activity,
                    AddNewSpotFragment(),
                    AddNewSpotFragment.Param(it.latitude, it.longitude),
                    Navigation.Animation.VERTICAL)
            }
        }

        fabCenterToLocation.setOnClickListener {
            lastKnowLocation?.let {
                val latlng = LatLng(it.latitude, it.longitude)
                val zoom = if (!initialZoom) 15f else googleMap!!.cameraPosition.zoom
                initialZoom = true
                setMapToLocation(latlng, zoom)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume() // needed to common.get the common.map to display immediately
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

    private fun initMaps(savedInstanceState: Bundle?) {
        mMapView = mapView
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume() // needed to common.get the common.map to display immediately
        MapsInitializer.initialize(activity.applicationContext)

        mMapView!!.getMapAsync { map ->
            googleMap = map
//            setupButtons()
            googleMap?.setOnMapLongClickListener(this)
            googleMap?.setOnMarkerClickListener(this)

            lastKnowLocation?.let {
                val latlng = LatLng(it.latitude, it.longitude)
                setMapToLocation(latlng)
                createMarkerOnPosition(latlng, null)
                initialZoom = true
            }
        }
    }

    override fun onMapLongClick(location: LatLng?) {
        location?.let {
            Navigation.switchFragments(activity,
                AddNewSpotFragment(),
                AddNewSpotFragment.Param(it.latitude, it.longitude),
                Navigation.Animation.VERTICAL)
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val sheet = SpotDetailBottomSheetFragment(marker?.tag as Spot, lastKnowLocation)
        sheet.show(childFragmentManager, "")
        return true
    }

    private fun setMapToLocation(location: LatLng, zoom: Float = 15f) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }

    private fun handlePermissions() {
        Permissions.check(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION, null,
            object : PermissionHandler() {
                override fun onGranted() { getAndSetLocation() }
                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    showErrorToast("Without this permission this app can't work properly!")
                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun getAndSetLocation() {
        val lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let { this@MapFragment.lastKnowLocation = it }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, listener)
        lastKnowLocation?.let { this.lastKnowLocation = it }
    }

    private fun createMarkerOnPosition(latLng: LatLng?, spot: Spot? = null) {
        latLng?.let {
            val marker = MarkerOptions()
                .position(LatLng(it.latitude, it.longitude))
            val pinIcon = bitmapDescriptorFromVector(R.drawable.ic_pin)
            marker.icon(pinIcon)
            val addedMarker = googleMap?.addMarker(marker)
            spot.let { addedMarker?.tag = it }
        }
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(requireContext(), vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

}