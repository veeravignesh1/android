package com.toggl.calendar.common.domain

import arrow.optics.optics
import com.toggl.environment.services.calendar.Calendar
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.TimeEntry
import java.time.OffsetDateTime

@optics
data class CalendarState(
    val timeEntries: Map<Long, TimeEntry>,
    val localState: LocalState
) {
    data class LocalState internal constructor(
        internal val selectedDate: OffsetDateTime,
        internal val calendarEvents: Map<String, CalendarEvent>,
        internal val selectedItem: SelectedCalendarItem?,
        internal val calendars: List<Calendar>
    ) {
        constructor() : this(OffsetDateTime.now(), mapOf(), null, listOf())
    }

    companion object
}

internal fun CalendarState.setSelectedItemToNull() =
    copy(localState = localState.copy(selectedItem = null))