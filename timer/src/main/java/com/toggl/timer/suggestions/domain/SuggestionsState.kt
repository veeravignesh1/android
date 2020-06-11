package com.toggl.timer.suggestions.domain

import arrow.optics.optics
import com.toggl.architecture.Loadable
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.User
import com.toggl.timer.common.domain.TimerState

@optics
data class SuggestionsState(
    val user: User,
    val projects: Map<Long, Project>,
    val timeEntries: Map<Long, TimeEntry>,
    val maxNumberOfSuggestions: Int
) {
    companion object {
        fun fromTimerState(timerState: TimerState): SuggestionsState? {

            val user = timerState.user as? Loadable.Loaded<User> ?: return null

            return SuggestionsState(
                user = user.value,
                projects = timerState.projects,
                timeEntries = timerState.timeEntries,
                maxNumberOfSuggestions = timerState.localState.maxNumberOfSuggestions
            )
        }

        fun toTimerState(timerState: TimerState, suggestionsState: SuggestionsState?) =
            suggestionsState?.let {
                timerState.copy(timeEntries = suggestionsState.timeEntries)
            } ?: timerState
    }
}