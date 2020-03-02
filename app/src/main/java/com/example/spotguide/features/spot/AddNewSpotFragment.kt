package com.example.spotguide.features.spot

import com.example.spotguide.R
import com.example.spotguide.core.base.BaseInputFragment
import com.example.spotguide.core.extension.getFullText
import com.example.spotguide.core.extension.loadImageFromImageModel
import com.example.spotguide.core.extension.stringFromRes
import com.example.spotguide.core.navigation.Navigation
import com.example.spotguide.features.galley.GalleryFragment
import com.example.spotguide.features.galley.logic.ImageModel
import com.example.spotguide.features.main.MainFragment
import com.example.spotguide.features.main.NamedFun
import com.example.spotguide.features.spot.logic.SpotStates
import com.example.spotguide.features.spot.logic.SpotViewModel
import com.example.spotguide.ui.ViewHolders
import com.example.spotguide.ui.action_bar.ActionBarParams
import com.example.spotguide.ui.action_bar.Image
import com.example.spotguide.ui.action_bar.Text
import com.example.spotguide.ui.adapters.BaseRecyclerViewAdapter
import com.example.spotguide.utils.BottomButtonsUtils
import com.example.spotguide.utils.GeoCoderUtils
import com.example.spotguide.utils.GroundRatingBarUtils
import com.example.spotguide.utils.StarRatingBarUtils
import com.google.android.gms.maps.model.LatLng
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.fragment_add_new_spot.*
import org.koin.android.ext.android.inject
import java.io.Serializable

class AddNewSpotFragment : BaseInputFragment<AddNewSpotFragment.Param>() {

    override val actionBarParams = ActionBarParams(
        leftIcon = Image { Navigation.pop(activity) },
        middleText = Text(R.string.spot_add_new_header.stringFromRes())
    )

    data class Param(
        val latitude: Double,
        val longitude: Double
    ) : Serializable

    override val layoutResId: Int
        get() = R.layout.fragment_add_new_spot

    private val spotViewModel: SpotViewModel by inject()

    private var newSpot = Spot()
    private val imagesAdapter: BaseRecyclerViewAdapter<ImageModel, ViewHolders.SpotImageModel> by lazy {
        BaseRecyclerViewAdapter(
            viewHolderClass = ViewHolders.SpotImageModel::class,
            bind = { v, m, p -> bindPhotos(v, m, p) }
        )
    }

    override fun setViewModelStates() {
        onStates(spotViewModel) { state ->
            when (state) {
                is SpotStates.AddingSpot -> activity.showLoading(state.text)
                is SpotStates.SpotAdded -> onSpotAdded()
            }
        }
    }

    override fun setViewModelEvents() {}

    override fun setupUI() {
        setupRatingBars()
        setupBottomButtons()
        getNameFromLocation()
        setupAddImages()
    }

    // Handling states section START

    private fun onSpotAdded() {
        showSuccessToast(R.string.spot_add_new_added.stringFromRes())
        Navigation.popToFragment(MainFragment::class, activity)
    }

    // Handling states section END

    private fun setupRatingBars() {
        GroundRatingBarUtils.setupView(vGroundRatingBar) { newSpot = newSpot.copy(groundRating = it) }
        StarRatingBarUtils.setupView(vStarRatingBar) { newSpot = newSpot.copy(rating = it) }
    }

    private fun setupBottomButtons() {
        val leftFunc =
            NamedFun(R.string.spot_add_new_cancel.stringFromRes()) {
                Navigation.pop(activity)
            }
        val rightFunc =
            NamedFun(R.string.spot_add_new_confirm.stringFromRes()) {
                spotViewModel.addSpot(createSpotInstance())
            }
        BottomButtonsUtils.setupButtons(vBottomButtons, leftFunc, rightFunc)
    }

    private fun createSpotInstance(): Spot {
        newSpot = newSpot.copy(
            name = etSpotName.getFullText(),
            latitude = params!!.latitude,
            longitude = params!!.longitude,
            description = etDescription.getFullText()
            )
        return newSpot
    }

    private fun getNameFromLocation() {
        tvAddress.text = GeoCoderUtils.getNameFromLocation(requireContext(),
            LatLng(params!!.latitude, params!!.longitude))
    }

    private fun setupAddImages() {
        ivAddPhotos.setOnClickListener {
            Navigation.switchFragments(activity,
                GalleryFragment(),
                GalleryFragment.Param(::onImagesPick))
        }
    }

    private fun onImagesPick(images: List<ImageModel>) {
        onFragmentLoaded.add(Event {
            imagesAdapter.addOnlyNewData(images)
        })
    }

    private fun bindPhotos(viewHolder: ViewHolders.SpotImageModel, model: ImageModel, position: Int) {
        viewHolder.photo.loadImageFromImageModel(model)
    }

}