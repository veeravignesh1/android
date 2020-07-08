package com.toggl.domain.mappings

import com.toggl.architecture.Loadable
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.models.domain.User
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsState

fun mapAppStateToSettingsState(appState: AppState): SettingsState? {
    val user = appState.user as? Loadable.Loaded<User> ?: return null
    return SettingsState(
        user.value,
        appState.userPreferences,
        appState.shouldRequestCalendarPermission,
        appState.backStack
    )
}

fun mapSettingsStateToAppState(appState: AppState, settingsState: SettingsState?): AppState =
    settingsState?.let { s ->
        appState.copy(
            user = Loadable.Loaded(s.user),
            userPreferences = s.userPreferences,
            shouldRequestCalendarPermission = s.shouldRequestCalendarPermission,
            backStack = s.backStack
        )
    } ?: appState

fun mapSettingsActionToAppAction(settingsAction: SettingsAction): AppAction =
    AppAction.Settings(settingsAction)
