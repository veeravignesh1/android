package com.toggl.settings.domain

import arrow.optics.optics
import com.toggl.models.domain.UserPreferences

@optics
sealed class SettingsAction {
    data class UserPreferencesUpdated(val userPreferences: UserPreferences) : SettingsAction()
    data class ManualModeToggled(val isManual: Boolean) : SettingsAction()

    companion object
}

fun SettingsAction.formatForDebug() =
    when (this) {
        is SettingsAction.UserPreferencesUpdated -> "User preferences updated: $userPreferences"
        is SettingsAction.ManualModeToggled -> "ManualModeToggled manual: $isManual"
    }