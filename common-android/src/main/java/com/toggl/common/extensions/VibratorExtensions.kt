package com.toggl.common.extensions

import android.annotation.SuppressLint
import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES.N_MR1
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.P
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import com.toggl.common.Constants.Vibration.defaultDurationInMillis
import com.toggl.common.Constants.Vibration.oldApisTickVibrationPattern
import com.toggl.common.Constants.Vibration.oldApisVibrationPattern
import com.toggl.common.Constants.Vibration.tickAmplitude
import com.toggl.common.Constants.Vibration.tickDurationInMillis

@SuppressLint("InlinedApi")
fun Vibrator.performClickEffect() =
    performEffect(VibrationEffect.EFFECT_CLICK, defaultDurationInMillis, DEFAULT_AMPLITUDE, oldApisVibrationPattern)

@SuppressLint("InlinedApi")
fun Vibrator.performTickEffect() =
    performEffect(VibrationEffect.EFFECT_TICK, tickDurationInMillis, tickAmplitude, oldApisTickVibrationPattern)

object AndroidApis {
    val current: Int = Build.VERSION.SDK_INT
}

/**
 * The NewApi is being suppressed because AS only understands that we are safe to make a call when doing something like
 * ```
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { ... }
 * ```
 * For sanity reasons, here's the list of required apis for each call:
 * VibrationEffect.createOneShot requires API > 26 (O)
 * Vibrator.vibrate(VibrationEffect) requires API > 26 (O)
 * VibrationEffect.createPredefined requires API > 29 (Q)
 *
 * Vibrator.vibrate(LongArray, repeatCount: Int) was deprecated
 */
@SuppressLint("MissingPermission", "NewApi")
@Suppress("DEPRECATION")
private fun Vibrator.performEffect(
    vibrationEffectForApiQ: Int,
    durationInMillisForApiO: Long,
    amplitudeForApiO: Int,
    vibrationVibrationPatternForPreO: LongArray
) {
    if (!hasVibrator()) return

    when (AndroidApis.current) {
        in LOLLIPOP..N_MR1 -> vibrate(vibrationVibrationPatternForPreO, -1)
        in O..P -> vibrate(VibrationEffect.createOneShot(durationInMillisForApiO, amplitudeForApiO))
        else -> vibrate(VibrationEffect.createPredefined(vibrationEffectForApiQ))
    }
}