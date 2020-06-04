package com.toggl.calendar.calendarday.ui.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class CalendarBackgroundDrawingDelegate {
    private val linesPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#c2c2c2")
        style = Paint.Style.STROKE
    }

    fun onDraw(canvas: Canvas, calendarBounds: RectF) {
        canvas.drawLine(0f, calendarBounds.centerY(), calendarBounds.right, calendarBounds.centerY(), linesPaint)
    }
}