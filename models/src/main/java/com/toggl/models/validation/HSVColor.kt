package com.toggl.models.validation

import com.toggl.common.Constants

data class HSVColor(val hue: Float, val saturation: Float, val value: Float) {
    companion object {
        val defaultCustomColor = HSVColor(
            hue = Constants.DefaultCustomColor.hue,
            saturation = Constants.DefaultCustomColor.saturation,
            value = Constants.DefaultCustomColor.value
        )
    }
}
