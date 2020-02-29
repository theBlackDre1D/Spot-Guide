package com.example.map.ui.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.spotguide.core.extension.dp

class LinearSpacingDecorator(private var space: Int = 16, private val orientation: Orientation): RecyclerView.ItemDecoration() {

    enum class Orientation { HORIZONTAL, VERTICAL }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (orientation) {
            Orientation.HORIZONTAL -> outRect.set(space.dp, 0, 0, 0) // converting to DP units - px is wrong extension name
            else -> outRect.set(0, space.dp, 0, 0) // converting to DP units - px is wrong extension name
        }

    }
}