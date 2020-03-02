package com.example.spotguide.features.galley.logic

import android.net.Uri
import android.provider.MediaStore
import com.example.spotguide.core.App
import io.uniflow.result.SafeResult
import java.util.*

class ImagesRepositoryImp : ImagesRepository {

    override suspend fun loadImages(): SafeResult<List<ImageModel>> {
        val context = App.currentActivity.get()!!
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
            MediaStore.MediaColumns.DATE_ADDED + " DESC")
        val images = mutableListOf<ImageModel>()

        try {
            cursor?.let {
                it.moveToFirst()
                for (i in 0 until it.count) {
                    cursor.moveToPosition(i)
                    val columnIndexId = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val columnIndexData = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val imageID = it.getInt(columnIndexId)
                    val imageData = it.getString(columnIndexData)
                    val imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageID.toString())
                    val im = ImageModel(
                        UUID.randomUUID().toString(),
                        imageUri,
                        imageData,
                        null
                    )
                    images.add(im)
                }
            }
        } catch (e: Exception) {
            // Nothing yet
        }
        cursor?.close()
        return SafeResult.success(images)
    }

}