package com.example.spotguide.core.extension

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.spotguide.core.App

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.onDone(onDone: (text: String) -> Unit) {
    this.imeOptions = EditorInfo.IME_ACTION_DONE
    this.setOnEditorActionListener { v, keyCode, event ->
        if (keyCode == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER) {
            onDone(this.text.toString())
            App.currentActivity.get()?.hideKeyboard()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun EditText.afterFocusChange(afterFocusChange: (Boolean) -> Unit) {
    this.onFocusChangeListener = object : View.OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            afterFocusChange(hasFocus)
        }
    }
}

fun EditText.getFullText() = this.text.toString()

fun EditText.showKeyboard() {
    this.requestFocus()
    this.setSelection(this.getFullText().count())
    (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun EditText.hideKeyboard() {
    val activity = App.currentActivity.get()
    activity?.hideKeyboard()
    this.clearFocus()
}