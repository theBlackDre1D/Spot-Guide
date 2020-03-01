package com.example.spotguide.utils

import android.view.View
import android.widget.ImageView
import com.example.spotguide.core.extension.dp
import kotlinx.android.synthetic.main.view_ground_rating_bar.view.*

object GroundRatingBarUtils {

    // Public section

    fun setupView(view: View, afterPick: (Int) -> Unit) {
        val emojies = getEmojies(view)
        emojies.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                view.requestLayout()
                changeRating(emojies, index)
                afterPick.invoke(index)
            }
        }
    }

    fun setupRating(view: View, rating: Int) {
        val emojies = getEmojies(view)
        changeRating(emojies, rating - 1)
    }

    // Private section

    private fun changeRating(emojies: List<ImageView>, position: Int) {
        emojies.forEachIndexed { index, imageView ->
            if (index == position) {
                imageView.layoutParams.height = 40.dp
                imageView.layoutParams.width = 40.dp
            } else {
                imageView.layoutParams.height = 30.dp
                imageView.layoutParams.width = 30.dp
            }
        }
    }

    private fun getEmojies(view: View): List<ImageView> {
        return listOf<ImageView>(view.ivSmile1, view.ivSmile2, view.ivSmile3, view.ivSmile4, view.ivSmile5)
    }

}