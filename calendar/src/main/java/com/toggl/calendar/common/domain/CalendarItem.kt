package com.toggl.calendar.common.domain

import com.toggl.calendar.exception.SelectedItemShouldBeATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldBeACalendarEventException
import com.toggl.calendar.exception.SelectedItemShouldNotBeNullException
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.TimeEntry
import java.time.Duration
import java.time.OffsetDateTime
import com.toggl.models.domain.EditableTimeEntry

typealias TimeEntryItem = TimeEntry
typealias CalendarEventItem = CalendarEvent

sealed class CalendarItem {
    abstract val columnIndex: Int
    abstract val totalColumns: Int

    data class TimeEntry(
        val timeEntry: TimeEntryItem,
        override val columnIndex: Int = 0,
        override val totalColumns: Int = 0
    ) : CalendarItem()

    data class CalendarEvent(
        val calendarEvent: CalendarEventItem,
        override val columnIndex: Int = 0,
        override val totalColumns: Int = 0
    ) : CalendarItem()
}

fun CalendarItem.startTime(): OffsetDateTime = when (this) {
    is CalendarItem.TimeEntry -> timeEntry.startTime
    is CalendarItem.CalendarEvent -> calendarEvent.startTime
}

fun CalendarItem.endTime(): OffsetDateTime? = when (this) {
    is CalendarItem.TimeEntry -> timeEntry.startTime + timeEntry.duration
    is CalendarItem.CalendarEvent -> calendarEvent.startTime + calendarEvent.duration
}

fun CalendarItem.duration(): Duration? = when (this) {
    is CalendarItem.TimeEntry -> timeEntry.duration
    is CalendarItem.CalendarEvent -> calendarEvent.duration
}

fun CalendarItem.toSelectedCalendarItem(): SelectedCalendarItem = when (this) {
    is CalendarItem.TimeEntry -> SelectedCalendarItem.SelectedTimeEntry(EditableTimeEntry.fromSingle(this.timeEntry))
    is CalendarItem.CalendarEvent -> SelectedCalendarItem.SelectedCalendarEvent(this.calendarEvent)
}

sealed class SelectedCalendarItem {
    data class SelectedTimeEntry(val editableTimeEntry: EditableTimeEntry) : SelectedCalendarItem()
    data class SelectedCalendarEvent(val calendarEvent: CalendarEventItem) : SelectedCalendarItem()
}

fun SelectedCalendarItem?.toEditableTimeEntry() =
    when (this) {
        null -> throw SelectedItemShouldNotBeNullException()
        is SelectedCalendarItem.SelectedCalendarEvent -> throw SelectedItemShouldBeATimeEntryException()
        is SelectedCalendarItem.SelectedTimeEntry -> editableTimeEntry
    }

fun SelectedCalendarItem?.toCalendarEvent() =
    when (this) {
        null -> throw SelectedItemShouldNotBeNullException()
        is SelectedCalendarItem.SelectedTimeEntry -> throw SelectedItemShouldBeACalendarEventException()
        is SelectedCalendarItem.SelectedCalendarEvent -> calendarEvent
    }