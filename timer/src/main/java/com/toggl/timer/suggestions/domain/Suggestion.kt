package com.toggl.timer.suggestions.domain

import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.TimeEntry

sealed class Suggestion {
    data class MostUsed(val timeEntry: TimeEntry)
    data class Calendar(val calendarEvent: CalendarEvent, val workspaceId: Long)
}