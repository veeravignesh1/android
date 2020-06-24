package com.toggl.repository.interfaces

import com.toggl.models.domain.UserPreferences

interface SettingsRepository {
    suspend fun loadUserPreferences(): UserPreferences
    suspend fun saveUserPreferences(userPreferences: UserPreferences)

    companion object {
        const val isManualModeEnabled = "isManualModeEnabled"
    }
}