package com.toggl.timer.suggestions.domain

import arrow.optics.optics
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.common.domain.TimerState

@optics
data class SuggestionsState(
    val timeEntries: Map<Long, TimeEntry>
) {
    companion object {
        fun fromTimerState(timerState: TimerState) =
            SuggestionsState(timeEntries = timerState.timeEntries)

        fun toTimerState(timerState: TimerState, suggestionsState: SuggestionsState) =
            timerState.copy(timeEntries = suggestionsState.timeEntries)
    }
}