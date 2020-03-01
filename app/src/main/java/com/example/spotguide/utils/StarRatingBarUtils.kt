package com.example.spotguide.utils

import android.view.View
import android.widget.ImageView
import com.example.spotguide.R
import kotlinx.android.synthetic.main.view_rating_bar.view.*

object StarRatingBarUtils {

    // Public section

    fun setupView(view: View, afterPick: (Int) -> Unit) {
        val emojies = getStars(view)
        emojies.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                changeRating(emojies, index)
                afterPick.invoke(index)
            }
        }
    }

    // Private section

    private fun changeRating(emojies: List<ImageView>, position: Int) {
        emojies.forEachIndexed { index, imageView ->
            if (index <= position) imageView.setImageResource(R.drawable.ic_star)
            else imageView.setImageResource(R.drawable.ic_empty_star)
        }
    }

    private fun getStars(view: View): List<ImageView> {
        return listOf<ImageView>(view.ivStar1, view.ivStar2, view.ivStar3, view.ivStar4, view.ivStar5)
    }

}