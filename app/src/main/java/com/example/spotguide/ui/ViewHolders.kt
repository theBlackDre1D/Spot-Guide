package com.example.spotguide.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.spotguide.R
import com.example.spotguide.features.spot.logic.Review
import kotlinx.android.synthetic.main.item_review.view.*
import kotlinx.android.synthetic.main.item_spot_photo.view.*

sealed class ViewHolders {

    class SpotPhoto(view: View) : BaseViewHolder<Int>(view, R.layout.item_spot_photo) {
        val photo = view.ivSpotPhoto
    }

    class ReviewCell(view: View) : BaseViewHolder<Review>(view, R.layout.item_review) {
        val stars = view.vStarRatingBar
        val reviewtext = view.tvReviewBody
    }

    abstract class BaseViewHolder<M: Any>(view: View, val viewResId: Int): RecyclerView.ViewHolder(view) {
        val rootView: View
            get() = itemView
    }
}