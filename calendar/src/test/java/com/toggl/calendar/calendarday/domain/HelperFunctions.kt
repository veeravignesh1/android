package com.toggl.calendar.calendarday.domain

import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.environment.services.calendar.Calendar
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun createCalendarDayState(
    timeEntries: Map<Long, TimeEntry> = mapOf(),
    projects: Map<Long, Project> = mapOf(),
    calendarEvents: Map<String, CalendarEvent> = mapOf(),
    selectedItem: SelectedCalendarItem? = null,
    date: OffsetDateTime = OffsetDateTime.of(2020, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC),
    calendars: List<Calendar> = listOf()
) = CalendarDayState(timeEntries, projects, calendarEvents, selectedItem, date, calendars)