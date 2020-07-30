package com.toggl.timer.project.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import com.toggl.common.Constants.ColorValue
import com.toggl.timer.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.roundToInt
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
class ColorValueSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    var hue: Float by Delegates.observable(0f) { _, old, new ->
        if (old != new) {
            updateView()
        }
    }

    var saturation: Float by Delegates.observable(0f) { _, old, new ->
        if (old != new) {
            updateView()
        }
    }

    val valueFlow: Flow<Float> = callbackFlow {
        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                offer(progress.toValue())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        awaitClose { setOnClickListener(null) }
    }

    var value: Float
        get() = progress.toValue()
        set(v) {
            if (v != progress.toValue()) {
                progress = v.toProgress()
            }
        }

    init {
        updateView()
        max = 100 - ColorValue.min.toProgress()
    }

    private fun updateView() {
        val gradientBackground = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.HSVToColor(floatArrayOf(hue * 360, saturation, ColorValue.max)),
                Color.HSVToColor(floatArrayOf(hue * 360, saturation, ColorValue.min))
            )
        ).apply {
            cornerRadius = 100f
        }
        progressDrawable = InsetDrawable(gradientBackground, resources.getDimension(R.dimen.color_value_picker_progress_background_margin).toInt())
    }

    private fun Int.toValue() = ColorValue.max - (toFloat() / 100f)
    private fun Float.toProgress() = (coerceIn(ColorValue.min, ColorValue.max) * 100).roundToInt()
}
