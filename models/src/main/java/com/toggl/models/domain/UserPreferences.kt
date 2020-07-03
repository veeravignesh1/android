package com.toggl.models.domain

import java.time.DayOfWeek

data class UserPreferences(
    val manualModeEnabled: Boolean,
    val twentyFourHourClockEnabled: Boolean,
    val groupSimilarTimeEntriesEnabled: Boolean,
    val cellSwipeActionsEnabled: Boolean,
    val calendarIntegrationEnabled: Boolean,
    val calendarIds: List<String>,
    val selectedWorkspaceId: Long,
    val dateFormat: DateFormat,
    val durationFormat: DurationFormat,
    val firstDayOfTheWeek: DayOfWeek,
    val smartAlertsOption: SmartAlertsOption
) {
    companion object {
        val default = UserPreferences(
            manualModeEnabled = false,
            twentyFourHourClockEnabled = false,
            groupSimilarTimeEntriesEnabled = true,
            cellSwipeActionsEnabled = true,
            calendarIntegrationEnabled = false,
            calendarIds = emptyList(),
            selectedWorkspaceId = 1,
            dateFormat = DateFormat.DDMMYYYY_dash,
            durationFormat = DurationFormat.Improved,
            firstDayOfTheWeek = DayOfWeek.MONDAY,
            smartAlertsOption = SmartAlertsOption.Disabled
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