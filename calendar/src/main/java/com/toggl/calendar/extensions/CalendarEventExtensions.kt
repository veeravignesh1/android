package com.toggl.calendar.extensions

import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.EditableTimeEntry

fun CalendarEvent.toEditableTimeEntry(workspaceId: Long) =
    EditableTimeEntry.empty(workspaceId).copy(
        description = description,
        duration = duration,
        startTime = startTime,
        billable = false
    )