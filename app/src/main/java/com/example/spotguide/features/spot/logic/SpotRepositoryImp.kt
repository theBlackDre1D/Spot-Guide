package com.example.spotguide.features.spot.logic

import com.example.spotguide.features.spot.Spot
import io.uniflow.result.SafeResult

class SpotRepositoryImp(
    private val provider: SpotFirestoreProvider
) : SpotRepository {

    override suspend fun getAllSpots(): SafeResult<List<Spot>> {
        val result = provider.getAllSpots()
        return result?.let { SafeResult.success(it) } ?: SafeResult.failure(Exception("Spots loading failure"))
    }

    override suspend fun addSpot(spot: Spot): SafeResult<Unit> {
        // TODO
        return SafeResult.success(Unit)
    }

}