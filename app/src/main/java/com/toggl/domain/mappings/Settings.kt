package com.toggl.domain.mappings

import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsState

fun mapAppStateToSettingsState(appState: AppState): SettingsState =
    SettingsState(appState.userPreferences, appState.selectedSetting)

fun mapSettingsStateToAppState(appState: AppState, settingsState: SettingsState): AppState =
    appState.copy(userPreferences = settingsState.userPreferences, selectedSetting = settingsState.selectedSetting)

fun mapSettingsActionToAppAction(settingsAction: SettingsAction): AppAction =
    AppAction.Settings(settingsAction)