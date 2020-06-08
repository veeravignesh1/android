package com.toggl.common.extensions

import android.content.res.ColorStateList
import android.view.View

fun View.setBackgroundTint(color: Int) {
    val colorStateList = ColorStateList.valueOf(color)
    this.backgroundTintList = colorStateList
}