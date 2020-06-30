package com.toggl.models.domain

import java.time.DayOfWeek

data class UserPreferences(
    val isManualModeEnabled: Boolean,
    val is24HourClockEnabled: Boolean,
    val isGroupSimilarTimeEntriesEnabled: Boolean,
    val isCellSwipeActionsEnabled: Boolean,
    val isCalendarIntegrationEnabled: Boolean,
    val selectedWorkspaceId: Long,
    val dateFormat: DateFormat,
    val durationFormat: DurationFormat,
    val firstDayOfTheWeek: DayOfWeek,
    val smartAlertsOption: SmartAlertsOption
) {
    companion object {
        val defaultUserPreferences = UserPreferences(
            isManualModeEnabled = false,
            is24HourClockEnabled = false,
            selectedWorkspaceId = 1,
            dateFormat = DateFormat.DDMMYYYY_dash,
            durationFormat = DurationFormat.Improved,
            firstDayOfTheWeek = DayOfWeek.MONDAY,
            isGroupSimilarTimeEntriesEnabled = true,
            isCellSwipeActionsEnabled = true,
            smartAlertsOption = SmartAlertsOption.Disabled,
            isCalendarIntegrationEnabled = false
        )
    }
}

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

enum class SmartAlertsOption {
    Disabled,
    WhenEventStarts,
    MinutesBefore5,
    MinutesBefore10,
    MinutesBefore15,
    MinutesBefore30,
    MinutesBefore60,
}