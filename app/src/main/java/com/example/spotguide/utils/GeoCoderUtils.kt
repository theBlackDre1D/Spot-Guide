package com.example.spotguide.utils

import android.content.Context
import android.location.Geocoder
import com.example.spotguide.R
import com.example.spotguide.core.extension.stringFromRes
import com.google.android.gms.maps.model.LatLng

object GeoCoderUtils {

    fun getNameFromLocation(context: Context, location: LatLng): String {
        val geocoder = Geocoder(context)
        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.first()
        address?.let {
            return "${it.locality ?: it.subLocality} ${it.premises}, ${it.countryCode}"
        } ?: run {
            return R.string.spot_add_new_unknown_location.stringFromRes()
        }
    }

}