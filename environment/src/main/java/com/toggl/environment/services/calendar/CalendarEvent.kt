package com.toggl.environment.services.calendar

import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

data class CalendarEvent(
    val id: String,
    val startTime: OffsetDateTime,
    val duration: Duration,
    val description: String,
    val color: String?,
    val calendarId: String
)