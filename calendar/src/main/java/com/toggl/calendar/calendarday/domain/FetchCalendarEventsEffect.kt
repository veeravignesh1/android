package com.toggl.calendar.calendarday.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.environment.services.calendar.Calendar
import com.toggl.environment.services.calendar.CalendarService
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime

class FetchCalendarEventsEffect(
    private val calendarService: CalendarService,
    private val fromStartDate: OffsetDateTime,
    private val toEndDate: OffsetDateTime,
    private val fromCalendars: List<Calendar>,
    private val dispatcherProvider: DispatcherProvider
) : Effect<CalendarDayAction.CalendarEventsFetched> {
    override suspend fun execute(): CalendarDayAction.CalendarEventsFetched? = withContext(dispatcherProvider.io) {
        calendarService.getCalendarEvents(fromStartDate, toEndDate, fromCalendars)
            .let(CalendarDayAction::CalendarEventsFetched)
    }
}