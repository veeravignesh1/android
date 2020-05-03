package com.toggl.timer.startedit.ui.editduration.shapes

import android.R.attr
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.annotation.ColorInt
import kotlin.math.sqrt

class HandleCap(
    private val radius: Float,
    arcWidth: Float,
    @ColorInt val capColor: Int,
    @ColorInt val capBorderColor: Int,
    @ColorInt val foregroundColor: Int,
    @ColorInt val capShadowColor: Int,
    private val capBorderStrokeWidth: Float,
    private val iconBitmap: Bitmap,
    @ColorInt val iconColor: Int,
    private val shadowWidth: Float
) {
    private val noFlags = 0
    private val capInnerSquareSide: Float = sqrt((attr.radius - shadowWidth) * (attr.radius - shadowWidth) * 2) * 0.5f
    private val arcRadius: Float = arcWidth / 2f
    private var shadowBitmap: Bitmap =
        Bitmap.createBitmap((radius * 2f).toInt(), (radius * 2f).toInt(), Bitmap.Config.ARGB_8888)
    private val position: PointF = PointF(Float.NaN, Float.NaN)
    private var showOnlyBackground = false

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = foregroundColor
    }
    private val capPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = capColor
    }
    private val capBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = capBorderStrokeWidth
        color = capBorderColor
    }
    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
    }
    private val shadowPaint = Paint(noFlags).apply {
        color = capShadowColor
        maskFilter = BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.NORMAL)
        style = Paint.Style.FILL
    }

    init {
        val shadowCanvas = Canvas(shadowBitmap)
        shadowCanvas.drawCircle(radius, radius, radius - shadowWidth, shadowPaint)
    }

    fun onDraw(canvas: Canvas?) = canvas?.run {
        if (position.x.isNaN()) return@run

        if (showOnlyBackground) {
            drawCircle(position.x, position.y, arcRadius, arcPaint)
            return@run
        }

        val innerSquareLeft = position.x - capInnerSquareSide
        val innerSquareTop = position.y - capInnerSquareSide
        drawBitmap(shadowBitmap, position.x - radius, position.y - radius, shadowPaint)
        drawCircle(position.x, position.y, radius - shadowWidth - capBorderStrokeWidth / 4f, capPaint)
        drawCircle(position.x, position.y, radius - shadowWidth, capBorderPaint)
        drawBitmap(
            iconBitmap,
            innerSquareLeft + (capInnerSquareSide * 2f - iconBitmap.width) / 2f,
            innerSquareTop + (capInnerSquareSide * 2f - iconBitmap.height) / 2f, iconPaint
        )
    }

    fun setPosition(newPosition: PointF) {
        position.set(newPosition)
    }

    fun setForegroundColor(@ColorInt color: Int) {
        arcPaint.color = color
    }

    fun setShowOnlyBackground(shouldOnlyShowBackground: Boolean) {
        this.showOnlyBackground = shouldOnlyShowBackground
    }
}
