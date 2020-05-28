package com.toggl.common.feature.timeentry.extensions

import com.toggl.models.domain.TimeEntry

fun Map<Long, TimeEntry>.replaceTimeEntryWithId(id: Long, timeEntryToReplace: TimeEntry): Map<Long, TimeEntry> =
    toMutableMap()
        .also { it[id] = timeEntryToReplace }
        .toMap()

fun Map<Long, TimeEntry>.runningTimeEntryOrNull() =
    this.values.firstOrNull { it.duration == null }