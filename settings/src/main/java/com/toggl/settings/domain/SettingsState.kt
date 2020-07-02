package com.toggl.settings.domain

import arrow.optics.optics
import com.toggl.common.feature.navigation.BackStack
import com.toggl.models.domain.UserPreferences

@optics
data class SettingsState(
    val userPreferences: UserPreferences,
    val shouldRequestCalendarPermission: Boolean,
    val backStack: BackStack
) {
    companion object
}