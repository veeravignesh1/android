package com.toggl.settings.domain

import arrow.optics.optics
import com.toggl.models.domain.UserPreferences

@optics
data class SettingsState(
    val userPreferences: UserPreferences,
    val selectedSetting: SelectedSetting?
) {
    companion object
}