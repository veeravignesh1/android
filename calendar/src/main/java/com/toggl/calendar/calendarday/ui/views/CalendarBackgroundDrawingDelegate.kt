package com.toggl.calendar.calendarday.ui.views

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.toggl.common.Constants.ClockMath.hoursInTheDay
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class CalendarBackgroundDrawingDelegate(
    private val hourLineColor: Int,
    private val hourLabelTextColor: Int,
    private val hourLabelTextSize: Float,
    private val verticalLineLeftMargin: Float,
    private val timeSliceStartX: Float,
    private val hoursX: Float
) {
    private val format = DateTimeFormatter.ofPattern("HH:mm")
    private var hours: List<String> = createHours()
    private var hoursYs: List<Float> = emptyList()
    private var timeLinesYs: List<Float> = emptyList()

    var currentHourHeight: Float = 0f
        set(value) {
            field = value
            onLayout()
        }

    private val linesPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = hourLineColor
        style = Paint.Style.STROKE
    }

    private val hoursLabelPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.RIGHT
        color = hourLabelTextColor
        textSize = hourLabelTextSize
    }

    fun onLayout() {
        timeLinesYs = createTimeLinesYPositions()
        hoursYs = timeLinesYs.map { lineY -> lineY + hoursLabelPaint.descent() }
    }

    fun onDraw(canvas: Canvas, calendarBounds: RectF) {
        canvas.drawLine(verticalLineLeftMargin, calendarBounds.top, verticalLineLeftMargin, calendarBounds.bottom, linesPaint)
        val timeLinesYsToDraw = timeLinesYs
        val hourLabelYsToDraw = hoursYs
        val hoursToDraw = hours
        for (hour in 1 until timeLinesYsToDraw.size) {
            val hourTop = hourLabelYsToDraw[hour] + linesPaint.ascent()
            val hourBottom = hourLabelYsToDraw[hour] + linesPaint.descent()
            if (hourBottom < calendarBounds.top || hourTop >= calendarBounds.bottom) continue
            canvas.drawLine(
                timeSliceStartX,
                timeLinesYsToDraw[hour],
                calendarBounds.width(),
                timeLinesYsToDraw[hour],
                linesPaint
            )
            canvas.drawText(hoursToDraw[hour], hoursX, hourLabelYsToDraw[hour], hoursLabelPaint)
        }
    }

    private fun createHours(): List<String> {
        val date = OffsetDateTime.now().toLocalDate()
        return (0 until hoursInTheDay)
            .map { date.atTime(it, 0) }
            .map { it.format(format) }
    }

    private fun createTimeLinesYPositions(): List<Float> {
        return (0 until hoursInTheDay)
            .map { line -> line * currentHourHeight }
    }
}