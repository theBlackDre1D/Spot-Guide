package com.example.spotguide.ui.action_bar

import com.example.spotguide.R


data class ActionBarParams(
    val actionBarVisible: Boolean = true,
    val leftIcon: Image? = null,
    val middleText: Text? = null,
    val rightText: Text? = null
)

data class Text(val text: String,
                val textColor: Int? = null,
                val onClick: (() -> Unit)? = null)

data class Image(val imageResId: Int = R.drawable.ic_back,
                 val onClick: () -> Unit)