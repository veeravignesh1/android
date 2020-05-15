package com.toggl.common

object Constants {
    const val timeEntryDeletionDelayMs = 5000L
    const val elapsedTimeIndicatorUpdateDelayMs = 1000L

    object Vibration {
        const val defaultDurationInMillis = 10L
        val oldApisVibrationPattern = longArrayOf(0, 10)

        const val tickDurationInMillis = 5L
        const val tickAmplitude = 5
        val oldApisTickVibrationPattern = longArrayOf(0, 5)
    }

    object TimeEntry {
        const val maxDurationInHours = 999L
    }

    object AutoCompleteSuggestions {
        const val projectToken: Char = '@'
    }
}
