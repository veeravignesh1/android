package com.toggl.timer.common.domain

import com.toggl.architecture.core.Reducer
import com.toggl.models.domain.TimeEntry

typealias TimerReducer = Reducer<TimerState, TimerAction>

fun handleTimeEntryCreationStateChanges(
    timeEntries: Map<Long, TimeEntry>,
    startedTimeEntry: TimeEntry,
    stoppedTimeEntry: TimeEntry?
): Map<Long, TimeEntry> {

    val newEntries = timeEntries.toMutableMap()
    newEntries[startedTimeEntry.id] = startedTimeEntry
    if (stoppedTimeEntry != null) {
        newEntries[stoppedTimeEntry.id] = stoppedTimeEntry
    }

    return newEntries.toMap()
}

fun handleTimeEntryDeletionStateChanges(
    timeEntries: Map<Long, TimeEntry>,
    deletedTimeEntry: TimeEntry
): Map<Long, TimeEntry> {

    val newEntries = timeEntries.toMutableMap()
    newEntries.remove(deletedTimeEntry.id)

    return newEntries.toMap()
}
