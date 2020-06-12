package com.toggl.calendar.common.domain

import com.toggl.calendar.exception.SelectedItemShouldBeACalendarEventException
import com.toggl.calendar.exception.SelectedItemShouldBeATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldNotBeNullException
import com.toggl.common.extensions.maybePlus
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.TimeEntry
import java.time.Duration
import java.time.OffsetDateTime

typealias TimeEntryItem = TimeEntry
typealias CalendarEventItem = CalendarEvent

sealed class CalendarItem {
    abstract val columnIndex: Int
    abstract val totalColumns: Int

    data class TimeEntry(
        val timeEntry: TimeEntryItem,
        val projectColor: String? = null,
        override val columnIndex: Int = 0,
        override val totalColumns: Int = 0
    ) : CalendarItem()

    data class CalendarEvent(
        val calendarEvent: CalendarEventItem,
        override val columnIndex: Int = 0,
        override val totalColumns: Int = 0
    ) : CalendarItem()

    data class SelectedItem(
        val selectedCalendarItem: SelectedCalendarItem,
        val color: String? = null,
        override val columnIndex: Int = 0,
        override val totalColumns: Int = 0
    ) : CalendarItem()
}

fun CalendarItem.colorString(): String? = when (this) {
    is CalendarItem.TimeEntry -> projectColor
    is CalendarItem.CalendarEvent -> calendarEvent.color
    is CalendarItem.SelectedItem -> when (selectedCalendarItem) {
        is SelectedCalendarItem.SelectedTimeEntry -> color
        is SelectedCalendarItem.SelectedCalendarEvent -> selectedCalendarItem.calendarEvent.color
    }
}

fun CalendarItem.description(): String = when (this) {
    is CalendarItem.TimeEntry -> timeEntry.description
    is CalendarItem.CalendarEvent -> calendarEvent.description
    is CalendarItem.SelectedItem -> when (selectedCalendarItem) {
        is SelectedCalendarItem.SelectedTimeEntry -> selectedCalendarItem.editableTimeEntry.description
        is SelectedCalendarItem.SelectedCalendarEvent -> selectedCalendarItem.calendarEvent.description
    }
}

fun CalendarItem.startTime(): OffsetDateTime = when (this) {
    is CalendarItem.TimeEntry -> timeEntry.startTime
    is CalendarItem.CalendarEvent -> calendarEvent.startTime
    is CalendarItem.SelectedItem -> when (selectedCalendarItem) {
        is SelectedCalendarItem.SelectedTimeEntry -> selectedCalendarItem.editableTimeEntry.startTime!!
        is SelectedCalendarItem.SelectedCalendarEvent -> selectedCalendarItem.calendarEvent.startTime
    }
}

fun CalendarItem.endTime(): OffsetDateTime? = when (this) {
    is CalendarItem.TimeEntry -> timeEntry.startTime.maybePlus(timeEntry.duration)
    is CalendarItem.CalendarEvent -> calendarEvent.startTime + calendarEvent.duration
    is CalendarItem.SelectedItem -> when (selectedCalendarItem) {
        is SelectedCalendarItem.SelectedTimeEntry -> {
            val editableTimeEntry = selectedCalendarItem.editableTimeEntry
            editableTimeEntry.startTime!!.maybePlus(editableTimeEntry.duration)
        }
        is SelectedCalendarItem.SelectedCalendarEvent ->
            selectedCalendarItem.calendarEvent.startTime + selectedCalendarItem.calendarEvent.duration
    }
}

fun CalendarItem.duration(): Duration? = when (this) {
    is CalendarItem.TimeEntry -> timeEntry.duration
    is CalendarItem.CalendarEvent -> calendarEvent.duration
    is CalendarItem.SelectedItem -> when (selectedCalendarItem) {
        is SelectedCalendarItem.SelectedTimeEntry -> selectedCalendarItem.editableTimeEntry.duration
        is SelectedCalendarItem.SelectedCalendarEvent -> selectedCalendarItem.calendarEvent.duration
    }
}

fun CalendarItem.isRunning(): Boolean =
    this is CalendarItem.TimeEntry && this.timeEntry.duration == null

fun CalendarItem.toSelectedCalendarItem(): SelectedCalendarItem = when (this) {
    is CalendarItem.TimeEntry -> SelectedCalendarItem.SelectedTimeEntry(EditableTimeEntry.fromSingle(this.timeEntry))
    is CalendarItem.CalendarEvent -> SelectedCalendarItem.SelectedCalendarEvent(this.calendarEvent)
    is CalendarItem.SelectedItem -> this.selectedCalendarItem
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