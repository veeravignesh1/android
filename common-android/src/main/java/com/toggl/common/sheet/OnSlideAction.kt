package com.toggl.common.sheet

import android.view.View
import androidx.annotation.FloatRange

interface OnSlideAction {
    fun onSlide(
        sheet: View,
        @FloatRange(
            from = -1.0,
            fromInclusive = true,
            to = 1.0,
            toInclusive = true
        ) slideOffset: Float
    )
}

class AlphaSlideAction(
    private val view: View,
    private val reverse: Boolean = false
) : OnSlideAction {
    override fun onSlide(sheet: View, slideOffset: Float) {
        view.alpha = if (!reverse) slideOffset else 1F - slideOffset
    }
}