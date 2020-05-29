package com.toggl.environment.services.calendar

import java.time.OffsetDateTime

interface CalendarService {
    fun getAvailableCalendars(): List<Calendar>
    fun getCalendarEvents(
        fromStartDate: OffsetDateTime,
        toEndDate: OffsetDateTime,
        fromCalendars: List<Calendar>
    ): List<CalendarEvent>
}
