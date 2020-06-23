package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.createCalendarEvent
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.environment.services.calendar.Calendar
import com.toggl.models.domain.Client
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry

fun createInitialState(
    timeEntries: List<TimeEntry> = emptyList(),
    projects: List<Project> = emptyList(),
    clients: List<Client> = emptyList(),
    calendars: List<Calendar> = emptyList(),
    editableTimeEntry: EditableTimeEntry? = null,
    selectedItem: SelectedCalendarItem = SelectedCalendarItem.SelectedCalendarEvent(calendarEvent = createCalendarEvent())
) = ContextualMenuState(
    timeEntries.associateBy { it.id },
    projects.associateBy { it.id },
    clients.associateBy { it.id },
    calendars,
    editableTimeEntry,
    selectedItem
)