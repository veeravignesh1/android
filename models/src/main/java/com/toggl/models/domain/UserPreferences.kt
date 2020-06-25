package com.toggl.models.domain

import java.time.DayOfWeek

data class UserPreferences(
    val isManualModeEnabled: Boolean,
    val is24HourClock: Boolean,
    val selectedWorkspaceId: Long,
    val dateFormat: DateFormat,
    val durationFormat: DurationFormat,
    val firstDayOfTheWeek: DayOfWeek
)

enum class DateFormat {
    MMDDYYYY_slash,
    DDMMYYYY_dash,
    MMDDYYYY_dash,
    YYYYMMDD_dash,
    DDMMYYYY_slash,
    DDMMYYYY_dot
}

enum class DurationFormat {
    Classic,
    Improved,
    Decimal
}