package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.SettingsRepository
import kotlinx.coroutines.withContext

class LoadUserPreferencesEffect(
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<LoadingAction> {
    override suspend fun execute(): LoadingAction? =
        withContext(dispatcherProvider.io) {
            LoadingAction.UserPreferencesLoaded(settingsRepository.loadUserPreferences())
        }
}
