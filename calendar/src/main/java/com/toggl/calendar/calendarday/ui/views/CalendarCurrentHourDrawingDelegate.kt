package com.toggl.calendar.calendarday.ui.views

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class CalendarCurrentHourDrawingDelegate(
    private val currentHourColor: Int,
    private val calendarCurrentHourIndicatorStrokeSize: Float,
    private val timeSliceStartX: Float,
    private val currentHourCircleRadius: Float
) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = currentHourColor
        strokeWidth = calendarCurrentHourIndicatorStrokeSize
        style = Paint.Style.FILL_AND_STROKE
    }

    fun onDraw(canvas: Canvas, calendarBounds: RectF, currentHourY: Float) {
        canvas.drawLine(timeSliceStartX, currentHourY, calendarBounds.right, currentHourY, paint)
        canvas.drawCircle(timeSliceStartX, currentHourY, currentHourCircleRadius, paint)
    }
}