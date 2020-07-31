package com.toggl.common.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

fun Activity.tryShowingKeyboardFor(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT) {
    getSystemService<InputMethodManager>()?.showSoftInput(view, flags)
}

fun Activity.tryHidingKeyboard(view: View) {
    getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(view.windowToken, 0)
}
