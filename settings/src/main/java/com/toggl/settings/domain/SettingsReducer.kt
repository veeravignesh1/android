package com.toggl.settings.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.models.domain.UserPreferences
import com.toggl.repository.interfaces.SettingsRepository
import javax.inject.Inject

class SettingsReducer @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<SettingsState, SettingsAction> {

    override fun reduce(
        state: MutableValue<SettingsState>,
        action: SettingsAction
    ): List<Effect<SettingsAction>> =
        when (action) {
            is SettingsAction.UserPreferencesUpdated -> state.mutateWithoutEffects { copy(userPreferences = action.userPreferences) }
            is SettingsAction.ManualModeToggled -> state.updateUserPreferences { copy(isManualModeEnabled = action.isManual) }
        }

    private fun MutableValue<SettingsState>.updateUserPreferences(updateBlock: UserPreferences.() -> UserPreferences) =
        effect(UpdateUserPreferencesEffect(this().userPreferences.updateBlock(), settingsRepository, dispatcherProvider))
}
