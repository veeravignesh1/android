package com.toggl.settings.domain

import arrow.optics.optics
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SelectedSetting
import com.toggl.models.domain.SmartAlertsOption
import com.toggl.models.domain.UserPreferences
import java.time.DayOfWeek

@optics
sealed class SettingsAction {
    data class UserPreferencesUpdated(val userPreferences: UserPreferences) : SettingsAction()
    data class SettingTapped(val selectedSetting: SelectedSetting) : SettingsAction()
    data class FeedbackEntered(val feedbackMessage: String) : SettingsAction()
    data class ManualModeToggled(val isManualEnabled: Boolean) : SettingsAction()
    data class Use24HourClockToggled(val is24HourClockEnabled: Boolean) : SettingsAction()
    data class CellSwipeActionsToggled(val isCellSwipeActionsEnabled: Boolean) : SettingsAction()
    data class GroupSimilarTimeEntriesToggled(val isGroupSimilarTimeEntriesEnabled: Boolean) : SettingsAction()
    data class CalendarIntegrationToggled(val isCalendarIntegrationEnabled: Boolean) : SettingsAction()
    data class WorkspaceSelected(val selectedWorkspaceId: Long) : SettingsAction()
    data class DateFormatSelected(val dateFormat: DateFormat) : SettingsAction()
    data class DurationFormatSelected(val durationFormat: DurationFormat) : SettingsAction()
    data class FirstDayOfTheWeekSelected(val firstDayOfTheWeek: DayOfWeek) : SettingsAction()
    data class SmartAlertsOptionSelected(val smartAlertsOption: SmartAlertsOption) : SettingsAction()

    companion object
}

fun SettingsAction.formatForDebug() =
    when (this) {
        is SettingsAction.UserPreferencesUpdated -> "User preferences updated: $userPreferences"
        is SettingsAction.SettingTapped -> "Settings tapped: $selectedSetting"
        is SettingsAction.FeedbackEntered -> "FeedbackEntered feedbackMessage: $feedbackMessage"
        is SettingsAction.ManualModeToggled -> "ManualModeToggled isManualEnabled: $isManualEnabled"
        is SettingsAction.Use24HourClockToggled -> "Use24HourClockToggled is24HourClockEnabled: $is24HourClockEnabled"
        is SettingsAction.WorkspaceSelected -> "WorkspaceSelected selectedWorkspaceId: $selectedWorkspaceId"
        is SettingsAction.DateFormatSelected -> "DateFormatSelected dateFormat: $dateFormat"
        is SettingsAction.DurationFormatSelected -> "DurationFormatSelected durationFormat: $durationFormat"
        is SettingsAction.FirstDayOfTheWeekSelected -> "FirstDayOfTheWeekSelected day: $firstDayOfTheWeek"
        is SettingsAction.GroupSimilarTimeEntriesToggled -> "GroupSimilarTimeEntriesToggled isGroupSimilarTimeEntriesEnabled: $isGroupSimilarTimeEntriesEnabled"
        is SettingsAction.CellSwipeActionsToggled -> "CellSwipeActionsToggled isCellSwipeActionsEnabled: $isCellSwipeActionsEnabled"
        is SettingsAction.SmartAlertsOptionSelected -> "SmartAlertsOptionSelected smartAlertsOption: $smartAlertsOption"
        is SettingsAction.CalendarIntegrationToggled -> "CalendarIntegrationToggled isCalendarIntegrationEnabled: $isCalendarIntegrationEnabled"
    }