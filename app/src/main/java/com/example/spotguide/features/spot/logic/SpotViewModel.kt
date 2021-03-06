package com.example.spotguide.features.spot.logic

import com.example.spotguide.R
import com.example.spotguide.core.extension.stringFromRes
import com.example.spotguide.features.spot.Spot
import io.uniflow.androidx.flow.AndroidDataFlow
import io.uniflow.core.flow.UIEvent
import io.uniflow.core.flow.UIState

sealed class SpotStates : UIState() {
    data class SpotsLoaded(val spots: List<Spot>) : SpotStates()
    object SpotsLoading : SpotStates()
    data class AddingSpot(val text: String = R.string.spot_add_new_adding.stringFromRes()) : SpotStates()
    object SpotAdded : SpotStates()
    data class AddingReview(val text: String = R.string.spot_detail_adding_review.stringFromRes()) : SpotStates()
    object ReviewAdded : SpotStates()
    data class ReviewsForSpot(val reviews: List<Review>) : SpotStates()
}

sealed class SpotEvents : UIEvent() {
    object SpotsLoadingFail : SpotEvents()
    object SpotAddFail : SpotEvents()
    object AddReviewFail : SpotEvents()
    object ReviewsForSpotLoadFail : SpotEvents()
}

class SpotViewModel(
    private val repo: SpotRepository
) : AndroidDataFlow() {

    init {
        setState { UIState.Empty }
    }

    fun getSpots() {
        setState { SpotStates.SpotsLoading }
        setState {
            repo.getAllSpots()
                .onFailure { sendEvent(SpotEvents.SpotsLoadingFail) }
                .toStateOrNull { SpotStates.SpotsLoaded(it) }
        }
    }

    fun addSpot(spot: Spot) {
        setState { SpotStates.AddingSpot() }
        setState {
            repo.addSpot(spot)
                .onFailure { sendEvent(SpotEvents.SpotAddFail) }
                .toStateOrNull { SpotStates.SpotAdded }
        }
    }

    fun addReview(review: Review) {
        setState { SpotStates.AddingReview() }
        setState {
            repo.addReview(review)
                .onFailure { sendEvent(SpotEvents.AddReviewFail) }
                .toStateOrNull { SpotStates.ReviewAdded }
        }
    }

    fun getReviewsForSpot(spotId: String) {
        setState {
            repo.getReviewsForSpot(spotId)
                .onFailure { sendEvent(SpotEvents.ReviewsForSpotLoadFail) }
                .toStateOrNull { SpotStates.ReviewsForSpot(it) }
        }
    }

}