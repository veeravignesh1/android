package com.toggl.calendar.calendarday.domain

import com.toggl.calendar.common.domain.CalendarAction
import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.environment.services.calendar.CalendarEvent
import java.time.OffsetDateTime

sealed class CalendarDayAction {
    data class ItemTapped(val calendarItem: CalendarItem) : CalendarDayAction()
    object CalendarViewAppeared : CalendarDayAction()
    data class CalendarEventsFetched(val calendarEvents: List<CalendarEvent>) : CalendarDayAction()
    data class EmptyPositionLongPressed(val startTime: OffsetDateTime) : CalendarDayAction()

    companion object {
        fun toCalendarAction(calendarDayAction: CalendarDayAction): CalendarAction =
            CalendarAction.CalendarDay(calendarDayAction)
    }
}

fun CalendarDayAction.formatForDebug() =
    when (this) {
        is CalendarDayAction.ItemTapped -> "Calendar item tapped: $calendarItem"
        is CalendarDayAction.CalendarViewAppeared -> "Calendar view appeared"
        is CalendarDayAction.CalendarEventsFetched -> "${calendarEvents.size} calendar events fetched"
        is CalendarDayAction.EmptyPositionLongPressed -> "Calendar empty position tapped on: $startTime"
    }