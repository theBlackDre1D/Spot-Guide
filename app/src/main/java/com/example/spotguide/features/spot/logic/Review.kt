package com.example.spotguide.features.spot.logic

import com.example.spotguide.features.main.IHasUploadModel

data class Review(
    val userId: String? = null,
    val rating: Int? = null,
    val reviewText: String? = null,
    val spotId: String? = null
) : IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "userId" to userId,
            "rating" to rating,
            "reviewText" to reviewText,
            "spotId" to spotId
        )
    }
}