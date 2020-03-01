package com.example.spotguide.features.spot

import com.example.spotguide.features.main.IHasUploadModel
import com.google.android.gms.maps.model.LatLng

data class Spot(
    val name: String? = null,
    val rating: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val groundRating: Int? = null,
    val description: String? = null
) : IHasUploadModel {

    val location: LatLng?
        get() {
            return latitude?.let { lat -> longitude?.let { lon -> LatLng(lat, lon) } }
        }

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "name" to name,
            "rating" to rating,
            "latitude" to latitude,
            "longitude" to longitude,
            "groundRating" to groundRating,
            "description" to description
        )
    }
}