package com.toggl.common

import android.content.Context
import android.os.Vibrator
import androidx.core.content.getSystemService

fun Context.performClickHapticFeedback() {
    val vibrator = getSystemService<Vibrator>()
    vibrator?.performClickEffect()
}