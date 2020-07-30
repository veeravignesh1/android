package com.toggl.timer.project.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.ColorUtils
import com.toggl.common.extensions.Colors
import com.toggl.timer.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.abs
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
class ColorHueSaturationPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circleRadius: Float = 0f
    private var circleDiameter: Float = 0f
    private val circleFillPaint = Paint().apply {
        style = Paint.Style.FILL
        flags = Paint.ANTI_ALIAS_FLAG
    }
    private val circleStrokePaint: Paint
    private val circleColor = Color.WHITE

    private val opacityBackgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        flags = Paint.ANTI_ALIAS_FLAG
    }

    private val brightnessPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            flags = Paint.ANTI_ALIAS_FLAG
            shader = LinearGradient(0f, 0f, 0f, height.toFloat(), intArrayOf(Color.TRANSPARENT, Color.WHITE), null, Shader.TileMode.CLAMP)
        }
    }

    private val gradientBackgroundPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            flags = Paint.ANTI_ALIAS_FLAG
            shader = LinearGradient(0f, 0f, width.toFloat(), 0f, Colors.defaultPalette, null, Shader.TileMode.CLAMP)
        }
    }

    private val roundedCornerClipPath by lazy {
        Path().apply {
            val radius = resources.getDimension(R.dimen.color_hue_saturation_picker_corner_radius)
            addRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), radius, radius, Path.Direction.CW)
        }
    }

    private val circleThumb = RectF(0f, 0f, 0f, 0f)

    val hueFlow = MutableStateFlow(0f)

    val saturationFlow = MutableStateFlow(0f)

    var hue: Float
        get() = hueFlow.value
        set(h) { hueFlow.value = h }

    var saturation: Float
        get() = saturationFlow.value
        set(s) { saturationFlow.value = s }

    var value: Float by Delegates.observable(1f) { _, oldVal, newValue ->
        if (newValue != oldVal) {
            val opacity = complement(value) * 255
            opacityBackgroundPaint.color = ColorUtils.setAlphaComponent(Color.BLACK, opacity.toInt())
            invalidate()
        }
    }

    init {
        circleDiameter = resources.getDimension(R.dimen.color_hue_saturation_picker_circle_diameter)
        circleRadius = circleDiameter / 2

        circleStrokePaint = Paint().apply {
            style = Paint.Style.STROKE
            color = circleColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = resources.getDimension(R.dimen.color_hue_saturation_picker_stroke_width)
            flags = Paint.ANTI_ALIAS_FLAG
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean =
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                updateLocationTo(e.x, e.y)
                true
            }
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                updateLocationTo(e.x, e.y)
                true
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                true
            }
            else -> super.onTouchEvent(e)
        }

    private fun updateLocationTo(x: Float, y: Float) {

        val pointX = x.coerceIn(0f, width.toFloat())
        val pointY = y.coerceIn(0f, height.toFloat())

        hue = pointX / width
        saturation = complement(pointY / height)

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {

        fun Canvas.drawGradientBackground() {
            drawPaint(gradientBackgroundPaint)
            drawPaint(brightnessPaint)
            drawPaint(opacityBackgroundPaint)
        }

        fun Canvas.drawThumbCircle() {
            val x = (width * hue) - circleRadius
            val y = height * complement(saturation) - circleRadius
            circleThumb.set(x, y, x + circleDiameter, y + circleDiameter)
            circleFillPaint.color = Color.HSVToColor(floatArrayOf(hue * 360, saturation, value))
            drawRoundRect(circleThumb, circleRadius, circleRadius, circleStrokePaint)
            drawRoundRect(circleThumb, circleRadius, circleRadius, circleFillPaint)
        }

        with(canvas) {
            clipPath(roundedCornerClipPath)
            drawGradientBackground()
            drawThumbCircle()
        }
    }

    private fun complement(number: Float) = abs(number - 1)
}
