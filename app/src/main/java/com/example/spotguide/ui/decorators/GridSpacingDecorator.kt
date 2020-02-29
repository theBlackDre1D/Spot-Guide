package com.example.map.ui.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.spotguide.core.extension.dp

class GridSpacingDecorator(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        val spaceInDp = spacing.dp

        if (includeEdge) {
            outRect.left = spaceInDp - column * spaceInDp / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spaceInDp / spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spaceInDp
            }
            outRect.bottom = spaceInDp // item bottom
        } else {
            outRect.left = column * spaceInDp / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right = spaceInDp - (column + 1) * spaceInDp / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spaceInDp // item top
            }
        }
    }
}