package com.example.spotguide.features.spot.logic

import com.example.spotguide.features.spot.Spot
import io.uniflow.result.SafeResult

interface SpotRepository {
    suspend fun getAllSpots(): SafeResult<List<Spot>>
    suspend fun addSpot(spot: Spot): SafeResult<Unit>
    suspend fun addReview(review: Review): SafeResult<Unit>
    suspend fun getReviewsForSpot(spotId: String): SafeResult<List<Review>>
}