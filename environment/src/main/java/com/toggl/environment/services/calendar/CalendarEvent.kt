package com.toggl.environment.services.calendar

import java.time.Duration
import java.time.OffsetDateTime

data class CalendarEvent(
    val id: String,
    val startTime: OffsetDateTime,
    val duration: Duration,
    val description: String,
    val color: String?,
    val calendarId: String
)