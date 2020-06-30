package com.toggl.settings.domain

import arrow.optics.optics
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SelectedSetting
import com.toggl.models.domain.UserPreferences
import java.time.DayOfWeek

@optics
sealed class SettingsAction {
    data class UserPreferencesUpdated(val userPreferences: UserPreferences) : SettingsAction()
    data class SettingTapped(val selectedSetting: SelectedSetting) : SettingsAction()
    data class ManualModeToggled(val isManual: Boolean) : SettingsAction()
    data class Use24HourClockToggled(val is24HourClock: Boolean) : SettingsAction()
    data class WorkspaceSelected(val selectedWorkspaceId: Long) : SettingsAction()
    data class DateFormatSelected(val dateFormat: DateFormat) : SettingsAction()
    data class DurationFormatSelected(val durationFormat: DurationFormat) : SettingsAction()
    data class FirstDayOfTheWeekSelected(val firstDayOfTheWeek: DayOfWeek) : SettingsAction()
    data class GroupSimilarTimeEntriesToggled(val shouldGroupSimilarTimeEntries: Boolean) : SettingsAction()
    data class CellSwipeActionsToggled(val hasCellSwipeActions: Boolean) : SettingsAction()
    data class FeedbackEntered(val feedbackMessage: String) : SettingsAction()

    companion object
}

fun SettingsAction.formatForDebug() =
    when (this) {
        is SettingsAction.UserPreferencesUpdated -> "User preferences updated: $userPreferences"
        is SettingsAction.SettingTapped -> "Settings tapped: $selectedSetting"
        is SettingsAction.ManualModeToggled -> "ManualModeToggled manual: $isManual"
        is SettingsAction.Use24HourClockToggled -> "Use24HourClockToggled is24HourClock: $is24HourClock"
        is SettingsAction.WorkspaceSelected -> "WorkspaceSelected workspaceId: $selectedWorkspaceId"
        is SettingsAction.DateFormatSelected -> "DateFormatSelected dateFormat: $dateFormat"
        is SettingsAction.DurationFormatSelected -> "DurationFormatSelected durationFormat: $durationFormat"
        is SettingsAction.FirstDayOfTheWeekSelected -> "FirstDayOfTheWeekSelected day: $firstDayOfTheWeek"
        is SettingsAction.GroupSimilarTimeEntriesToggled -> "GroupSimilarTimeEntriesToggled shouldGroupSimilarTimeEntries: $shouldGroupSimilarTimeEntries"
        is SettingsAction.CellSwipeActionsToggled -> "CellSwipeActionsToggled hasCellSwipeActions: $hasCellSwipeActions"
        is SettingsAction.FeedbackEntered -> "FeedbackEntered feedbackMessage: $feedbackMessage"
    }