package com.toggl.timer.running.domain

import arrow.optics.optics
import com.toggl.common.feature.navigation.BackStack
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.common.domain.TimerState

@optics
data class RunningTimeEntryState(
    val backStack: BackStack,
    val timeEntries: Map<Long, TimeEntry>
) {
    companion object {
        fun fromTimerState(timerState: TimerState) =
            RunningTimeEntryState(
                backStack = timerState.backStack,
                timeEntries = timerState.timeEntries
            )

        fun toTimerState(timerState: TimerState, runningTimeEntryState: RunningTimeEntryState) =
            timerState.copy(
                backStack = runningTimeEntryState.backStack,
                timeEntries = runningTimeEntryState.timeEntries
            )
    }
}