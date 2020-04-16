package com.toggl.common

import android.annotation.SuppressLint
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

@SuppressLint("MissingPermission")
fun Vibrator.performClickEffect() {
    if (!hasVibrator())
        return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        return
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createOneShot(Constants.Vibration.defaultDurationInMillis, VibrationEffect.DEFAULT_AMPLITUDE))
        return
    }

    @Suppress("DEPRECATION")
    vibrate(Constants.Vibration.oldApisVibrationPattern, -1)
}