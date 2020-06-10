package com.toggl.common.feature.extensions

import android.graphics.Color
import com.toggl.models.validation.HSVColor

fun HSVColor.toColor() =
    Color.HSVToColor(floatArrayOf(hue * 360, saturation, value))

fun HSVColor.toHex() =
    String.format("#%06X", 0xFFFFFF and this.toColor())
