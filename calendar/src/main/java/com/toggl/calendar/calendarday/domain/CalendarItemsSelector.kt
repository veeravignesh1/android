package com.toggl.calendar.calendarday.domain

import com.toggl.architecture.core.Selector
import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.common.extensions.maybePlus
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarItemsSelector @Inject constructor(
    private val calendarLayoutCalculator: CalendarLayoutCalculator
) : Selector<CalendarDayState, List<CalendarItem>> {

    override suspend fun select(state: CalendarDayState): List<CalendarItem> {
        val localDate = state.date.toLocalDate()
        val projects = state.projects
        fun isOnDate(startTime: OffsetDateTime, endTime: OffsetDateTime?) =
            startTime.toLocalDate() == localDate && (endTime == null || endTime.toLocalDate() == localDate)

        val filteredTimeEntries = state.timeEntries.filterValues { isOnDate(it.startTime, it.startTime.maybePlus(it.duration)) }
        val filteredEvents = state.events.filterValues { isOnDate(it.startTime, it.startTime + it.duration) }
        return filteredTimeEntries
            .values.map { CalendarItem.TimeEntry(it, projectColor = projects[it.projectId]?.color) }
            .plus(filteredEvents.values.map { CalendarItem.CalendarEvent(it) })
            .run(calendarLayoutCalculator::calculateLayoutAttributes)
    }
}
