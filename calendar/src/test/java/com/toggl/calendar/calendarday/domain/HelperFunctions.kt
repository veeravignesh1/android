package com.toggl.calendar.calendarday.domain

import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.common.feature.navigation.Route
import com.toggl.environment.services.calendar.Calendar
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import java.time.OffsetDateTime

fun createInitialState(
    timeEntries: List<TimeEntry> = listOf(),
    calendarEvents: List<CalendarEvent> = listOf(),
    projects: List<Project> = listOf(),
    selectedItem: SelectedCalendarItem? = null,
    date: OffsetDateTime = OffsetDateTime.now(),
    calendars: List<Calendar> = listOf()
) = CalendarDayState(
    timeEntries.associateBy { it.id },
    projects.associateBy { it.id },
    if (selectedItem == null) emptyList() else listOf(Route.ContextualMenu(selectedItem)),
    calendarEvents.associateBy { it.id },
    date,
    calendars
)