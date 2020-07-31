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

enum class DateFormat(val label: String) {
    MMDDYYYY_slash("MM/DD/YYYY"),
    DDMMYYYY_dash("DD-MM-YYYY"),
    MMDDYYYY_dash("MM-DD-YYYY"),
    YYYYMMDD_dash("YYYY-MM-DD"),
    DDMMYYYY_slash("DD/MM/YYYY"),
    DDMMYYYY_dot("DD.MM.YYYY")
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
