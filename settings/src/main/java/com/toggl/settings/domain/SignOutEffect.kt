package com.toggl.settings.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.AppRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignOutEffect @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val appRepository: AppRepository
) : Effect<SettingsAction.SignOutCompleted> {
    override suspend fun execute(): SettingsAction.SignOutCompleted? =
        withContext(dispatcherProvider.io) {
            appRepository.clearAllData()
            SettingsAction.SignOutCompleted
        }
}
