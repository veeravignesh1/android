package com.toggl.timer.suggestions

import com.toggl.models.domain.TimeEntry
import com.toggl.timer.suggestions.domain.SuggestionsState

fun createInitialState(
    timeEntries: List<TimeEntry> = emptyList()
) = SuggestionsState(
    timeEntries = timeEntries.associateBy { it.id }
)