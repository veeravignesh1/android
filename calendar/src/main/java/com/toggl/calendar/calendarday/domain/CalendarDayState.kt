package com.toggl.calendar.calendarday.domain

import arrow.optics.optics
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.models.domain.TimeEntry
import java.time.OffsetDateTime

@optics
data class CalendarDayState(
    val timeEntries: Map<Long, TimeEntry>,
    val events: Map<String, CalendarEvent>,
    val selectedItem: SelectedCalendarItem?,
    val date: OffsetDateTime
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            CalendarDayState(
                calendarState.timeEntries,
                calendarState.localState.calendarEvents,
                calendarState.localState.selectedItem,
                calendarState.localState.selectedDate
            )

        fun toCalendarState(calendarState: CalendarState, calendarDayState: CalendarDayState) =
            calendarState.copy(
                timeEntries = calendarDayState.timeEntries,
                localState = calendarState.localState.copy(
                    calendarEvents = calendarDayState.events,
                    selectedItem = calendarDayState.selectedItem,
                    selectedDate = calendarDayState.date
                )
            )
    }
}