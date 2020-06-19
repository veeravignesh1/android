package com.toggl.timer.suggestions.domain

import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.log.domain.getProjectViewModelFor

sealed class Suggestion {
    data class MostUsed(val timeEntry: TimeEntry) : Suggestion()
    data class Calendar(val calendarEvent: CalendarEvent, val workspaceId: Long) : Suggestion()
}

fun Suggestion.toSuggestionViewModel(
    projects: Map<Long, Project>,
    clients: Map<Long, Client>
) = when (this) {
    is Suggestion.MostUsed -> {
        val project = projects.getProjectViewModelFor(this.timeEntry, clients)
        SuggestionViewModel(
            "MostUsed-${this.timeEntry.id}",
            this.timeEntry.description,
            project,
            this
        )
    }
    is Suggestion.Calendar -> {
        SuggestionViewModel(
            "Calendar-${this.calendarEvent.id}",
            this.calendarEvent.description,
            null,
            this
        )
    }
}