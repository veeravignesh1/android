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
            is SettingsAction.FeedbackEntered -> state.mutateWithoutEffects { copy(feedbackMessage = action.feedbackMessage) }
            is SettingsAction.ManualModeToggled -> state.updatePrefs { copy(isManualModeEnabled = action.isManualEnabled) }
            is SettingsAction.Use24HourClockToggled -> state.updatePrefs { copy(is24HourClockEnabled = action.is24HourClockEnabled) }
            is SettingsAction.CellSwipeActionsToggled -> state.updatePrefs { copy(isCellSwipeActionsEnabled = action.isCellSwipeActionsEnabled) }
            is SettingsAction.CalendarIntegrationToggled -> state.updatePrefs { copy(isCalendarIntegrationEnabled = action.isCalendarIntegrationEnabled) }
            is SettingsAction.GroupSimilarTimeEntriesToggled -> state.updatePrefs { copy(isGroupSimilarTimeEntriesEnabled = action.isGroupSimilarTimeEntriesEnabled) }
            is SettingsAction.WorkspaceSelected -> state.updatePrefs { copy(selectedWorkspaceId = action.selectedWorkspaceId) }
            is SettingsAction.DateFormatSelected -> state.updatePrefs { copy(dateFormat = action.dateFormat) }
            is SettingsAction.DurationFormatSelected -> state.updatePrefs { copy(durationFormat = action.durationFormat) }
            is SettingsAction.FirstDayOfTheWeekSelected -> state.updatePrefs { copy(firstDayOfTheWeek = action.firstDayOfTheWeek) }
            is SettingsAction.SmartAlertsOptionSelected -> state.updatePrefs { copy(smartAlertsOption = action.smartAlertsOption) }
        }

    private fun MutableValue<SettingsState>.updatePrefs(updateBlock: UserPreferences.() -> UserPreferences) =
        effect(UpdateUserPreferencesEffect(this().userPreferences.updateBlock(), settingsRepository, dispatcherProvider))
}
