package com.toggl.common.feature.services.calendar

import java.time.OffsetDateTime

interface CalendarService {
    suspend fun getAvailableCalendars(): List<Calendar>
    suspend fun getUserSelectedCalendars(): List<Calendar>
    suspend fun getCalendarEvents(
        fromStartDate: OffsetDateTime,
        toEndDate: OffsetDateTime,
        fromCalendars: List<Calendar>
    ): List<CalendarEvent>
}
