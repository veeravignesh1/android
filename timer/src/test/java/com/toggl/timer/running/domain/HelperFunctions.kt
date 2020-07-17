package com.toggl.timer.running.domain

import com.toggl.common.feature.navigation.getRouteParam
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.suggestions.domain.createUser

fun createInitialState(
    timeEntries: Map<Long, TimeEntry> = mapOf()
) =
    RunningTimeEntryState(
        user = createUser(),
        backStack = emptyList(),
        timeEntries = timeEntries
    )

val RunningTimeEntryState.editableTimeEntry: EditableTimeEntry?
    get() = backStack.getRouteParam<EditableTimeEntry>()