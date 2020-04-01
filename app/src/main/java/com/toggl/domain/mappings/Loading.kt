package com.toggl.domain.mappings

import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.domain.loading.LoadingAction
import com.toggl.domain.loading.LoadingState

fun mapAppStateToLoadingState(appState: AppState): LoadingState =
    LoadingState(
        projects = appState.projects.values,
        clients = appState.clients.values,
        workspaces = appState.workspaces.values,
        timeEntries = appState.timeEntries.values
    )

fun mapAppActionToLoadingAction(appAction: AppAction): LoadingAction? =
    if (appAction is AppAction.Loading) appAction.loading else null

fun mapLoadingStateToAppState(appState: AppState, loadingState: LoadingState): AppState =
    appState.copy(
        projects = loadingState.projects.associateBy { it.id },
        clients = loadingState.clients.associateBy { it.id },
        workspaces = loadingState.workspaces.associateBy { it.id },
        timeEntries = loadingState.timeEntries.associateBy { it.id }
    )

fun mapLoadingActionToAppAction(loadingAction: LoadingAction): AppAction =
    AppAction.Loading(loadingAction)
