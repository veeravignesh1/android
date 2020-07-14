package com.toggl.settings.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.SettingsRepository
import kotlinx.coroutines.withContext

class SignOutEffect(
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<SettingsAction.SignOutCompleted> {
    override suspend fun execute(): SettingsAction.SignOutCompleted? =
        withContext(dispatcherProvider.io) {
            settingsRepository.signOut()
            SettingsAction.SignOutCompleted
        }
}