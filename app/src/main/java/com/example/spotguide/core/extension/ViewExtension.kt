package com.example.spotguide.core.extension

import android.view.View
import android.view.ViewGroup

// returning itself to allow chainable calling methods in Stevia layout
fun View.visibleOrGone(visible: Boolean, animated: Boolean = false): View {
    setVisibility(this, visible, true, animated)
    return this
}
fun View.visibleOrGone(visible: Any?, animated: Boolean = false): View {
    visible?.let { setVisibility(this, true, true, animated) }
        ?: run { setVisibility(this, false, true, animated) }
    return this
}

fun View.visibleOrInvisible(visible: Boolean, animated: Boolean = false): View {
    setVisibility(this, visible, false, animated)
    return this
}

fun View.setLeftMargin(margin: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.leftMargin = margin
        this.requestLayout()
    }
}

fun View.setRightMargin(margin: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.rightMargin = margin
        this.requestLayout()
    }
}

fun View.setTopMargin(margin: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.topMargin = margin
        this.requestLayout()
    }
}

fun View.setBottomMargin(margin: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.bottomMargin = margin
        this.requestLayout()
    }
}

fun View.setMargins(l: Int, t: Int, r: Int, b: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(l, t, r, b)
        this.requestLayout()
    }
}

private fun setVisibility(view: View, visible: Boolean, gone: Boolean = false, animated: Boolean = false){
    val isVisible = view.visibility == View.VISIBLE

    val show = {
        view.visibility = View.VISIBLE
    }

    val hide = {
        if (gone) view.visibility = View.GONE
        else view.visibility = View.INVISIBLE
    }

    when {
        animated && visible && !isVisible -> {
            view.alpha = 0f
            show()
            view.animate().alpha(1f)
        }
        animated && !visible && isVisible -> {
            view.animate().alpha(0f).withEndAction {
                hide()
                view.alpha = 1f
            }
        }
        else -> {
            if(visible) show() else hide()
        }
    }
}