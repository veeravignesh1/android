package com.toggl.timer.common.domain

import arrow.optics.optics
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry

@optics
data class TimerState(
    val timeEntries: Map<Long, TimeEntry>,
    val projects: Map<Long, Project>,
    val localState: LocalState
) {
    data class LocalState internal constructor(
        internal val editedDescription: String,
        internal val editedTimeEntry: TimeEntry?
    ) {
        constructor() : this("", null)

        companion object
    }

    companion object
}
