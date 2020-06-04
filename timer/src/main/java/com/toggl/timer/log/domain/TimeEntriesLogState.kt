package com.toggl.timer.log.domain

import arrow.optics.optics
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.timer.common.domain.TimerState

@optics
data class TimeEntriesLogState(
    val timeEntries: Map<Long, TimeEntry>,
    val projects: Map<Long, Project>,
    val clients: Map<Long, Client>,
    val editableTimeEntry: EditableTimeEntry?,
    val expandedGroupIds: Set<Long>,
    val entriesPendingDeletion: Set<Long>,
    val shouldGroup: Boolean = true
) {
    companion object {
        fun fromTimerState(timerState: TimerState) =
            TimeEntriesLogState(
                timerState.timeEntries,
                timerState.projects,
                timerState.clients,
                timerState.editableTimeEntry,
                timerState.localState.expandedGroupIds,
                timerState.localState.entriesPendingDeletion
            )

        fun toTimerState(timerState: TimerState, timeEntriesLogState: TimeEntriesLogState) =
            timerState.copy(
                timeEntries = timeEntriesLogState.timeEntries,
                editableTimeEntry = timeEntriesLogState.editableTimeEntry,
                localState = timerState.localState.copy(
                    expandedGroupIds = timeEntriesLogState.expandedGroupIds,
                    entriesPendingDeletion = timeEntriesLogState.entriesPendingDeletion
                )
            )
    }
}
