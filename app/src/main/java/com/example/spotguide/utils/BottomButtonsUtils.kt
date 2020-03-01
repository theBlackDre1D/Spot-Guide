package com.example.spotguide.utils

import android.view.View
import com.example.spotguide.NamedFun
import kotlinx.android.synthetic.main.view_bottom_buttons_horizontal.view.*

object BottomButtonsUtils {

    fun setupButtons(view: View, left: NamedFun, right: NamedFun) {
        val leftButton = view.bLeft
        leftButton.text = left.name
        leftButton.setOnClickListener { left.func.invoke() }

        val rightButton = view.bRight
        rightButton.text = right.name
        rightButton.setOnClickListener { right.func.invoke() }
    }

}