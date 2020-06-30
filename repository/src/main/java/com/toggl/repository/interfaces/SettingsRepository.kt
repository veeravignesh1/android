package com.toggl.repository.interfaces

import com.toggl.models.domain.UserPreferences

interface SettingsRepository {
    suspend fun loadUserPreferences(): UserPreferences
    suspend fun saveUserPreferences(userPreferences: UserPreferences)

    companion object {
        const val isManualModeEnabled = "isManualModeEnabled"
        const val selectedWorkspaceId = "selectedWorkspaceId"
        const val is24HourClock = "is24HourClock"
        const val dateFormat = "dateFormat"
        const val durationFormat = "durationFormat"
        const val firstDayOfTheWeek = "firstDayOfTheWeek"
        const val shouldGroupSimilarTimeEntries = "shouldGroupSimilarTimeEntries"
        const val hasCellSwipeActions = "hasCellSwipeActions"
        const val smartAlertsOption = "smartAlertsOption"
    }
}