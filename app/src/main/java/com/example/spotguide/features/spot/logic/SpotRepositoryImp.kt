package com.example.spotguide.features.spot.logic

import com.example.spotguide.features.spot.Spot
import io.uniflow.result.SafeResult

class SpotRepositoryImp(
    private val provider: SpotFirestoreProvider,
    private val reviewProvider: ReviewFirestoreProvider
) : SpotRepository {

    override suspend fun getAllSpots(): SafeResult<List<Spot>> {
        val result = provider.getAllSpots()
        return result?.let { SafeResult.success(it) } ?: SafeResult.failure(Exception("Spots loading failure"))
    }

    override suspend fun addSpot(spot: Spot): SafeResult<Unit> {
        val result = provider.addSpot(spot)
        return result?.let { SafeResult.success(it) } ?: SafeResult.failure(Exception("Spots loading failure"))
    }

    override suspend fun addReview(review: Review): SafeResult<Unit> {
        val result = reviewProvider.addReview(review)
        return result?.let { SafeResult.success(it) } ?: SafeResult.failure(Exception("Review adding failure"))
    }

    override suspend fun getReviewsForSpot(spotId: String): SafeResult<List<Review>> {
        val result = reviewProvider.getReviewsForSpot(spotId)
        return result?.let { SafeResult.success(it) } ?: SafeResult.failure(Exception("Spots loading failure"))
    }

}