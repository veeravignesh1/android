package com.toggl.calendar.calendarday.domain

import arrow.optics.optics
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.common.feature.services.calendar.CalendarEvent
import com.toggl.common.feature.navigation.BackStack
import com.toggl.common.feature.services.calendar.Calendar
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import java.time.OffsetDateTime

@optics
data class CalendarDayState(
    val timeEntries: Map<Long, TimeEntry>,
    val projects: Map<Long, Project>,
    val backStack: BackStack,
    val events: Map<String, CalendarEvent>,
    val date: OffsetDateTime,
    val calendars: List<Calendar>
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            CalendarDayState(
                calendarState.timeEntries,
                calendarState.projects,
                calendarState.backStack,
                calendarState.calendarEvents,
                calendarState.localState.selectedDate,
                calendarState.localState.calendars
            )

        fun toCalendarState(calendarState: CalendarState, calendarDayState: CalendarDayState) =
            calendarState.copy(
                timeEntries = calendarDayState.timeEntries,
                projects = calendarDayState.projects,
                backStack = calendarDayState.backStack,
                calendarEvents = calendarDayState.events,
                localState = calendarState.localState.copy(
                    selectedDate = calendarDayState.date,
                    calendars = calendarDayState.calendars
                )
            )
    }
}