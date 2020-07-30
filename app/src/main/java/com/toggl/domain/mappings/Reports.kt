package com.toggl.domain.mappings

import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.reports.domain.ReportsAction
import com.toggl.reports.domain.ReportsState

fun mapAppStateToReportsState(appState: AppState): ReportsState =
    ReportsState(appState.workspaces)

@Suppress("UNUSED_PARAMETER")
fun mapReportsStateToAppState(appState: AppState, reportsState: ReportsState): AppState =
    appState

fun mapReportsActionToAppAction(reportsAction: ReportsAction): AppAction =
    AppAction.Reports(reportsAction)
