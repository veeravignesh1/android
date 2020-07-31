package com.toggl.common.extensions

import android.content.Context
import android.content.res.Configuration
import android.os.Vibrator
import androidx.core.content.getSystemService

fun Context.performClickHapticFeedback() {
    val vibrator = getSystemService<Vibrator>()
    vibrator?.performClickEffect()
}

val Context.isInDarkMode: Boolean
    get() =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES