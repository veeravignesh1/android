package com.toggl.repository.interfaces

import com.toggl.models.domain.UserPreferences

interface SettingsRepository {
    suspend fun loadUserPreferences(): UserPreferences
    suspend fun saveUserPreferences(userPreferences: UserPreferences)

    companion object {
        const val manualModeEnabled = "manualModeEnabled"
        const val twentyFourHourClockEnabled = "twentyFourHourClockEnabled"
        const val groupSimilarTimeEntriesEnabled = "groupSimilarTimeEntriesEnabled"
        const val cellSwipeActionsEnabled = "cellSwipeActionsEnabled"
        const val calendarIntegrationEnabled = "calendarIntegrationEnabled"
        const val calendarIds = "calendarIds"
        const val selectedWorkspaceId = "selectedWorkspaceId"
        const val smartAlertsOption = "smartAlertsOption"
        const val dateFormat = "dateFormat"
        const val durationFormat = "durationFormat"
        const val firstDayOfTheWeek = "firstDayOfTheWeek"
        const val apiToken = "apiToken"
    }
}
