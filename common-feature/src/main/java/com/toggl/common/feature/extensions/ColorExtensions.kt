package com.toggl.common.feature.extensions

import android.graphics.Color
import com.toggl.models.validation.HSVColor

fun HSVColor.toColor() =
    Color.HSVToColor(floatArrayOf(hue, saturation, value))