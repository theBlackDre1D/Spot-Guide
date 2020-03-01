package com.example.spotguide.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.spotguide.R
import kotlinx.android.synthetic.main.item_spot_photo.view.*

sealed class ViewHolders {

    class SpotPhoto(view: View) : BaseViewHolder<Int>(view) {

        override val viewResId = R.layout.item_spot_photo

        val photo = view.ivSpotPhoto
    }

    abstract class BaseViewHolder<M: Any>(view: View): RecyclerView.ViewHolder(view), IHasViewResId {
        val rootView: View
            get() = itemView
    }

    interface IHasViewResId {
        val viewResId: Int
    }
}