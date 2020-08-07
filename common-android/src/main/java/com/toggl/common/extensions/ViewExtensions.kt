package com.toggl.common.extensions

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

fun View.setOvalBackground(color: Int) {
    this.background = GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.OVAL
    }
}

fun View.adjustPaddingToStatusBarInsets() {
    setOnApplyWindowInsetsListener { _, insets ->
        updatePadding(top = paddingTop + insets.systemWindowInsetTop)
        setOnApplyWindowInsetsListener(null)
        insets
    }
}

fun View.adjustMarginToNavigationBarInsets() {
    setOnApplyWindowInsetsListener { _, insets ->
        updateLayoutParams<ViewGroup.MarginLayoutParams> { bottomMargin += insets.systemWindowInsetBottom }
        setOnApplyWindowInsetsListener(null)
        insets
    }
}
