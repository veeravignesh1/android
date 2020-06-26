package com.toggl.timer.running.domain

import com.toggl.common.feature.navigation.getRouteParam
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.TimeEntry

fun createInitialState(
    timeEntries: Map<Long, TimeEntry> = mapOf()
) =
    RunningTimeEntryState(
        backStack = emptyList(),
        timeEntries = timeEntries
    )

val RunningTimeEntryState.editableTimeEntry: EditableTimeEntry?
    get() = backStack.getRouteParam<EditableTimeEntry>()