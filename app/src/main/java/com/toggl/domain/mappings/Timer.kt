package com.toggl.domain.mappings

import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.timer.common.domain.TimerAction
import com.toggl.timer.common.domain.TimerState

fun mapAppStateToTimerState(appState: AppState): TimerState =
    TimerState(
        appState.user,
        appState.tags,
        appState.tasks,
        appState.clients,
        appState.projects,
        appState.workspaces,
        appState.timeEntries,
        appState.editableTimeEntry,
        appState.timerLocalState
    )

fun mapTimerStateToAppState(appState: AppState, timerState: TimerState): AppState =
    appState.copy(
        tags = timerState.tags,
        tasks = timerState.tasks,
        clients = timerState.clients,
        projects = timerState.projects,
        workspaces = timerState.workspaces,
        timeEntries = timerState.timeEntries,
        editableTimeEntry = timerState.editableTimeEntry,
        timerLocalState = timerState.localState
    )

fun mapTimerActionToAppAction(timerAction: TimerAction): AppAction =
    AppAction.Timer(timerAction)
