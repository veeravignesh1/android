package com.toggl.calendar.calendarday.domain

import com.toggl.calendar.common.domain.CalendarAction
import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.environment.services.calendar.CalendarEvent

sealed class CalendarDayAction {
    data class ItemTapped(val calendarItem: CalendarItem) : CalendarDayAction()
    object CalendarViewAppeared : CalendarDayAction()
    data class CalendarEventsFetched(val calendarEvents: List<CalendarEvent>) : CalendarDayAction()

    companion object {
        fun fromCalendarAction(calendarAction: CalendarAction): CalendarDayAction? =
            if (calendarAction !is CalendarAction.CalendarDay) null
            else calendarAction.calendarDay

        fun toCalendarAction(calendarDayAction: CalendarDayAction): CalendarAction =
            CalendarAction.CalendarDay(calendarDayAction)
    }
}

fun CalendarDayAction.formatForDebug() =
    when (this) {
        is CalendarDayAction.ItemTapped -> "Calendar item tapped: $calendarItem"
        is CalendarDayAction.CalendarViewAppeared -> "Calendar view appeared"
        is CalendarDayAction.CalendarEventsFetched -> "${calendarEvents.size} calendar events fetched"
    }