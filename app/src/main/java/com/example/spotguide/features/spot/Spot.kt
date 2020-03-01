package com.example.spotguide.features.spot

import com.google.android.gms.maps.model.LatLng

data class Spot(
    val name: String? = null,
    val rating: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val groundRating: Int? = null,
    val description: String? = null
) {

    val location: LatLng?
        get() {
            return latitude?.let { lat -> longitude?.let { lon -> LatLng(lat, lon) } }
        }
}