package com.toggl.timer.startedit.ui.editduration.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import androidx.annotation.ColorInt
import com.toggl.timer.startedit.util.toPositiveAngle
import com.toggl.timer.startedit.util.updateWithPointOnCircumference

class Dot(
    private val pivotCenter: PointF,
    private val distanceToPivot: Float,
    private val radius: Float,
    @ColorInt color: Int
) {
    private val visibilityThresholdInDegrees: Int = 15
    private val position = PointF(Float.NaN, Float.NaN)
    private var hidden = false
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
    }

    fun onDraw(canvas: Canvas?) = canvas?.run {
        if (hidden || position.x.isNaN()) return@run

        drawCircle(position.x, position.y, radius, paint)
    }

    fun update(startTimeAngle: Double, endTimeAngle: Double) {
        val diffAngle = (endTimeAngle - startTimeAngle).toPositiveAngle()
        val diffInDegrees = Math.toDegrees(diffAngle)
        hidden = diffInDegrees < visibilityThresholdInDegrees
        position.updateWithPointOnCircumference(pivotCenter, startTimeAngle + diffAngle / 2f, distanceToPivot.toDouble())
    }
}
