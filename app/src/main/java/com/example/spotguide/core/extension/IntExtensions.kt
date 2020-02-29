package com.example.spotguide.core.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.spotguide.core.App

fun Int.colorFromRes(context: Context) = context.resources.getColor(this)

fun Int.stringFromRes(): String = if (this < 1) "" else
    App.currentActivity.get()!!.applicationContext.getString(this)

fun Int.stringFromRes(context: Context): String = if (this < 1) "" else
    context.applicationContext.getString(this)

fun Int.stringFromRes(vararg formats: Any): String = if (this < 1) "" else
    App.currentActivity.get()!!.applicationContext.getString(this, *formats)

fun Int.stringQuantityFromRes(quantity: Int, vararg formats: Any): String = if (this < 1) "" else
    App.currentActivity.get()!!.applicationContext.resources.getQuantityString(this, quantity, *formats)

fun Int.drawableFromRes(): Drawable =
    ContextCompat.getDrawable(App.currentActivity.get()!!.applicationContext, this)!!

fun Int.colorFromRes(): Int =
    ContextCompat.getColor(App.currentActivity.get()!!.applicationContext, this)

val Int.px: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()