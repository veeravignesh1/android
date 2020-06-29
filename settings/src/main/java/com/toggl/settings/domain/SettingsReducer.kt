package com.toggl.settings.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.push
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
            is SettingsAction.SettingTapped -> state.mutateWithoutEffects {
                val editRoute = Route.SettingsEdit(action.selectedSetting)
                copy(backStack = backStack.push(editRoute))
            }
            is SettingsAction.ManualModeToggled -> state.updateUserPreferences { copy(isManualModeEnabled = action.isManual) }
            is SettingsAction.Use24HourClockToggled -> state.updateUserPreferences { copy(is24HourClock = action.is24HourClock) }
            is SettingsAction.WorkspaceSelected -> state.updateUserPreferences { copy(selectedWorkspaceId = action.selectedWorkspaceId) }
            is SettingsAction.DateFormatSelected -> state.updateUserPreferences { copy(dateFormat = action.dateFormat) }
            is SettingsAction.DurationFormatSelected -> state.updateUserPreferences { copy(durationFormat = action.durationFormat) }
            is SettingsAction.FirstDayOfTheWeekSelected -> state.updateUserPreferences { copy(firstDayOfTheWeek = action.firstDayOfTheWeek) }
            is SettingsAction.GroupSimilarTimeEntriesToggled -> state.updateUserPreferences { copy(shouldGroupSimilarTimeEntries = action.shouldGroupSimilarTimeEntries) }
            is SettingsAction.CellSwipeActionsToggled -> state.updateUserPreferences { copy(hasCellSwipeActions = action.hasCellSwipeActions) }
        }

    private fun MutableValue<SettingsState>.updateUserPreferences(updateBlock: UserPreferences.() -> UserPreferences) =
        effect(UpdateUserPreferencesEffect(this().userPreferences.updateBlock(), settingsRepository, dispatcherProvider))
}
