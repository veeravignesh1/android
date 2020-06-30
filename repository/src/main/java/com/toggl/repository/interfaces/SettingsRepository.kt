package com.toggl.repository.interfaces

import com.toggl.models.domain.UserPreferences

interface SettingsRepository {
    suspend fun loadUserPreferences(): UserPreferences
    suspend fun saveUserPreferences(userPreferences: UserPreferences)

    companion object {
        const val isManualModeEnabled = "isManualModeEnabled"
        const val is24HourClockEnabled = "is24HourClockEnabled"
        const val isGroupSimilarTimeEntriesEnabled = "isGroupSimilarTimeEntriesEnabled"
        const val isCellSwipeActionsEnabled = "isCellSwipeActionsEnabled"
        const val isCalendarIntegrationEnabled = "isCalendarIntegrationEnabled"
        const val selectedWorkspaceId = "selectedWorkspaceId"
        const val smartAlertsOption = "smartAlertsOption"
        const val dateFormat = "dateFormat"
        const val durationFormat = "durationFormat"
        const val firstDayOfTheWeek = "firstDayOfTheWeek"
    }
}