package com.toggl.settings.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.models.domain.UserPreferences
import com.toggl.repository.interfaces.SettingsRepository
import kotlinx.coroutines.withContext

class UpdateUserPreferencesEffect(
    private val newUserPreferences: UserPreferences,
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<SettingsAction.UserPreferencesUpdated> {

    override suspend fun execute(): SettingsAction.UserPreferencesUpdated =
        withContext(dispatcherProvider.io) {
            settingsRepository.saveUserPreferences(newUserPreferences)
            SettingsAction.UserPreferencesUpdated(newUserPreferences)
        }
}
