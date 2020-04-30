package com.toggl.timer.startedit.ui.editduration.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.ColorInt
import com.toggl.timer.startedit.util.toPositiveAngle

class Arc(
    private val bounds: RectF,
    strokeWidth: Float,
    @ColorInt fillColor: Int
) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.strokeWidth = strokeWidth
        color = fillColor
        style = Paint.Style.STROKE
    }

    private var startAngle: Double = 0.0
    private var endAngle: Double = 0.0
    private var endStroke: Double = 0.0

    fun setFillColor(@ColorInt color: Int) {
        paint.color = color
    }

    fun update(startTimeAngle: Double, endTimeAngle: Double) {
        startAngle = startTimeAngle
        endAngle = endTimeAngle
        endStroke = (endAngle - startAngle).toPositiveAngle()
    }

    fun onDraw(canvas: Canvas?) = canvas?.run {
        val startAngleInDegrees = Math.toDegrees(startAngle).toFloat()
        val endStrokeInDegrees = Math.toDegrees(endStroke).toFloat()
        drawArc(bounds, startAngleInDegrees, endStrokeInDegrees, false, paint)
    }
}
