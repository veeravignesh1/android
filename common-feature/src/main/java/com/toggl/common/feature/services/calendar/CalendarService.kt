package com.toggl.common.feature.services.calendar

import com.toggl.models.domain.UserPreferences
import java.time.OffsetDateTime

interface CalendarService {
    suspend fun getAvailableCalendars(): List<Calendar>
    suspend fun getUserSelectedCalendars(userPreferences: UserPreferences): List<Calendar>
    suspend fun getCalendarEvents(
        fromStartDate: OffsetDateTime,
        toEndDate: OffsetDateTime,
        fromCalendars: List<Calendar>
    ): List<CalendarEvent>
}
