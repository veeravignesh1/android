package com.toggl.common

import java.time.Duration

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
        val defaultTimeEntryDuration: Duration = Duration.ofMinutes(30)
    }

    object AutoCompleteSuggestions {
        const val projectToken: Char = '@'
        const val tagToken: Char = '#'
    }
}
