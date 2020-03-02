package com.example.spotguide.features.galley.logic

import com.example.spotguide.R
import com.example.spotguide.core.extension.stringFromRes
import io.uniflow.androidx.flow.AndroidDataFlow
import io.uniflow.core.flow.UIEvent
import io.uniflow.core.flow.UIState

sealed class ImagesStates : UIState() {
    data class ImagesLoaded(val photos: List<ImageModel>) : ImagesStates()
    data class ImagesLoading(val text: String = R.string.gallery_images_loading.stringFromRes()) : ImagesStates()
}

object ImagesFailure : UIEvent()

class ImagesViewModel(
    private val repo: ImagesRepository
) : AndroidDataFlow() {

    fun loadImages() {
        setState { ImagesStates.ImagesLoading() }
        setState {
            repo.loadImages()
                .onFailure { sendEvent(ImagesFailure) }
                .toStateOrNull { ImagesStates.ImagesLoaded(it) }
        }
    }

}