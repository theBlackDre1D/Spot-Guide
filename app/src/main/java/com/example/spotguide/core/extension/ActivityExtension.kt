package com.example.spotguide.core.extension

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.hideKeyboard(){
    val inputMethodManager = getSystemService (Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (currentFocus != null)
        inputMethodManager.hideSoftInputFromWindow (currentFocus!!.windowToken, 0)
}

