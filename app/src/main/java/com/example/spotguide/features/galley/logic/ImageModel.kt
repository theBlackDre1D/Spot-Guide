package com.example.spotguide.features.galley.logic

import android.graphics.Bitmap
import android.net.Uri

data class ImageModel(
    val id: String?,
    val uri: Uri?,
    val path: String?,
    var bitmap: Bitmap?
)