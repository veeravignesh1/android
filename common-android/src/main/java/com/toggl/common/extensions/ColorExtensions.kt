package com.toggl.common.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import kotlin.math.max
import kotlin.math.min

fun Int.getHSV(): Triple<Float, Float, Float> {
    val hsvArray = floatArrayOf(0F, 0F, 0F)
    Color.colorToHSV(this, hsvArray)

    return Triple(
        hsvArray[0],
        hsvArray[1],
        hsvArray[2]
    )
}

fun String.adjustForUserTheme(context: Context): Int =
    Color.parseColor(this).adjustForUserTheme(context)

fun String.toLabelColor(context: Context): Int =
    Color.parseColor(this).toLabelColor(context)

fun Int.adjustForUserTheme(context: Context): Int {
    if (!context.isInDarkMode)
        return this

    val (hue, saturation, value) = getHSV()

    val newSaturation = adjustSaturationToDarkMode(saturation, value)
    val newValue = adjustValueToDarkMode(value)

    val hsv = floatArrayOf(hue, newSaturation, newValue)
    return Color.HSVToColor(hsv)
}

fun Int.toLabelColor(context: Context): Int {
    val (hue, saturation, value) = getHSV()

    val isUsingDarkMode = context.isInDarkMode
    val (newSaturation, newValue) =
        if (isUsingDarkMode) adjustSaturationToDarkMode(saturation, value) to min(adjustValueToDarkMode(value) + .05F, 1.0F)
        else saturation to max(value - .15F, 0F)

    val hsv = floatArrayOf(hue, newSaturation, newValue)
    return Color.HSVToColor(hsv)
}

fun Int.toColorStateList() =
    ColorStateList.valueOf(this)

fun Int.toHex() =
    String.format("#%06X", 0xFFFFFF and this)

object Colors {
    val defaultPalette = intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED)
}

private fun adjustSaturationToDarkMode(saturation: Float, value: Float) =
    (saturation * value) / 1F

private fun adjustValueToDarkMode(value: Float) =
    (2F + value) / 3F
