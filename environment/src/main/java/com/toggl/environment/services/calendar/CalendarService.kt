package com.toggl.environment.services.calendar

import org.threeten.bp.OffsetDateTime

interface CalendarService {
    fun getAvailableCalendars(): List<Calendar>
    fun getCalendarEvents(
        fromStartDate: OffsetDateTime,
        toEndDate: OffsetDateTime,
        fromCalendars: List<Calendar>
    ): List<CalendarEvent>
}
