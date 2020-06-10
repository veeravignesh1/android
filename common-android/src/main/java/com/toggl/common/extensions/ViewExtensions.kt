package com.toggl.common.extensions

import android.graphics.drawable.GradientDrawable
import android.view.View

fun View.setOvalBackground(color: Int) {
    this.background = GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.OVAL
    }
}