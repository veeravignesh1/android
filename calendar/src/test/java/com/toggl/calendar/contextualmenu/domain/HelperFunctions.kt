package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.createCalendarEvent
import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.common.feature.navigation.Route
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
    selectedItem = selectedItem,
    backStack = if (editableTimeEntry != null) listOf(Route.StartEdit(editableTimeEntry)) else emptyList(),
    projects = projects.associateBy { it.id },
    clients = clients.associateBy { it.id },
    calendars = calendars
)