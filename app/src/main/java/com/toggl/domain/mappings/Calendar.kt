package com.toggl.domain.mappings

import com.toggl.calendar.common.domain.CalendarAction
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.domain.AppAction
import com.toggl.domain.AppState

fun mapAppStateToCalendarState(appState: AppState): CalendarState =
    CalendarState(
        appState.timeEntries,
        appState.projects,
        appState.clients,
        appState.editableTimeEntry,
        appState.calendarLocalState
    )

fun mapCalendarStateToAppState(appState: AppState, calendarState: CalendarState): AppState =
    appState.copy(
        projects = calendarState.projects,
        timeEntries = calendarState.timeEntries,
        editableTimeEntry = calendarState.editableTimeEntry,
        calendarLocalState = calendarState.localState
    )

fun mapCalendarActionToAppAction(timerAction: CalendarAction): AppAction =
    AppAction.Calendar(timerAction)
