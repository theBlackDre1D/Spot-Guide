package com.example.spotguide.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.spotguide.R
import com.example.spotguide.features.spot.Spot

sealed class ViewHolders {

    class ContactRow(view: View) : BaseViewHolder<Spot>(view) {

        override val viewResId = R.layout.item_spot
//        val delete = view.tvDelete
//        val checkbox = view.chbAdded
//        val contactName = view.tvContactName
//        val contactInfo = view.tvContactInfo
    }

    abstract class BaseViewHolder<M: Any>(view: View): RecyclerView.ViewHolder(view), IHasViewResId {
        val rootView: View
            get() = itemView
    }

    interface IHasViewResId {
        val viewResId: Int
    }
}