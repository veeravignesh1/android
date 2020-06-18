package com.toggl.calendar.calendarday.ui.views

import android.animation.TypeEvaluator
import android.graphics.RectF

class RectFEvaluator(private val reuseRectF: RectF) : TypeEvaluator<RectF> {
    override fun evaluate(fraction: Float, startValue: RectF, endValue: RectF): RectF {
        val left = startValue.left + (endValue.left - startValue.left) * fraction
        val top = startValue.top + (endValue.top - startValue.top) * fraction
        val right = startValue.right + (endValue.right - startValue.right) * fraction
        val bottom = startValue.bottom + (endValue.bottom - startValue.bottom) * fraction

        reuseRectF.set(left, top, right, bottom)
        return reuseRectF
    }
}

class RectFSideEvaluator(private val reuseRectF: RectF) : TypeEvaluator<RectF> {
    override fun evaluate(fraction: Float, startValue: RectF, endValue: RectF): RectF {
        val left = startValue.left + (endValue.left - startValue.left) * fraction
        val right = startValue.right + (endValue.right - startValue.right) * fraction

        reuseRectF.left = left
        reuseRectF.right = right
        return reuseRectF
    }
}