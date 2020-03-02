package com.example.spotguide.features.galley.logic

import io.uniflow.result.SafeResult

interface ImagesRepository {
    suspend fun loadImages(): SafeResult<List<ImageModel>>
}