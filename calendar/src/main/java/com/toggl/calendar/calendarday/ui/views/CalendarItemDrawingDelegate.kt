package com.toggl.calendar.calendarday.ui.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.toggl.calendar.common.domain.CalendarItem

class CalendarItemDrawingDelegate {
    private val itemPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#c2c2c2")
    }
    private val drawingRect: RectF = RectF()

    fun onDraw(canvas: Canvas, calendarBounds: RectF, calendarItem: CalendarItem) {
        calendarItem.run {
            drawingRect.set(calendarBounds.left, calendarBounds.top, calendarBounds.right, 300f)
            canvas.drawRect(drawingRect, itemPaint)
        }
    }
}