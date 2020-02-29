package com.example.spotguide.core.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference

fun ImageView.loadImageFromFirebase(storageRef: StorageReference, path: String?, placeholder: Int? = null, afterLoad: (() -> Unit)? = null): ImageView {
    try {
        placeholder?.let { this.setImageResource(it) }
        path?.let { p ->
            if (p.isNotEmpty()) {
                storageRef.child(p).getBytes(Long.MAX_VALUE).addOnSuccessListener {
                    try {
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        Glide.with(context)
                            .load(bitmap)
                            .into(this)
                        afterLoad?.invoke()
                    } catch (e: Exception) {}
                }.addOnFailureListener {}
            }
        }
    } catch (e: Exception) { return this }

    return this
}

fun ImageView.loadImageFromUri(uri: Uri): ImageView {
    Glide.with(context)
        .load(uri)
        .into(this)
    return this
}

fun ImageView.loadBitmapImage(bitmap: Bitmap?): ImageView {
    Glide.with(context)
        .load(bitmap)
        .into(this)
    return this
}

fun ImageView.loadImageFromResId(@DrawableRes imageId: Int): ImageView {
    Glide.with(context)
        .load(imageId.drawableFromRes())
        .into(this)
    return this
}

//fun ImageView.loadImageFromImageModel(model: ImageModel?) {
//    model?.uri?.let { this.loadImageFromUri(it) }
//        ?: run { this.loadBitmapImage(model?.bitmap) }
//}