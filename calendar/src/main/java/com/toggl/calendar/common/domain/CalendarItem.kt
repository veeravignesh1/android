package com.toggl.calendar.common.domain

import com.toggl.calendar.exception.SelectedItemShouldBeACalendarEventException
import com.toggl.calendar.exception.SelectedItemShouldBeATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldNotBeNullException
import com.toggl.common.extensions.maybePlus
import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.common.feature.timeentry.extensions.isRunning
import com.toggl.common.feature.services.calendar.CalendarEvent
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.TimeEntry
import java.time.Duration
import java.time.OffsetDateTime
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

val CalendarItem.colorString: String?
    get() = when (this) {
        is CalendarItem.TimeEntry -> projectColor
        is CalendarItem.CalendarEvent -> calendarEvent.color
        is CalendarItem.SelectedItem -> when (selectedCalendarItem) {
            is SelectedCalendarItem.SelectedTimeEntry -> color
            is SelectedCalendarItem.SelectedCalendarEvent -> selectedCalendarItem.calendarEvent.color
        }
    }

val CalendarItem.description: String
    get() = when (this) {
        is CalendarItem.TimeEntry -> timeEntry.description
        is CalendarItem.CalendarEvent -> calendarEvent.description
        is CalendarItem.SelectedItem -> selectedCalendarItem.description
    }

val CalendarItem.startTime: OffsetDateTime
    get() = when (this) {
        is CalendarItem.TimeEntry -> timeEntry.startTime
        is CalendarItem.CalendarEvent -> calendarEvent.startTime
        is CalendarItem.SelectedItem -> selectedCalendarItem.startTime
    }

val CalendarItem.endTime: OffsetDateTime?
    get() = when (this) {
        is CalendarItem.TimeEntry -> timeEntry.startTime.maybePlus(timeEntry.duration)
        is CalendarItem.CalendarEvent -> calendarEvent.startTime + calendarEvent.duration
        is CalendarItem.SelectedItem -> selectedCalendarItem.endTime
    }

val CalendarItem.duration: Duration?
    get() = when (this) {
        is CalendarItem.TimeEntry -> timeEntry.duration
        is CalendarItem.CalendarEvent -> calendarEvent.duration
        is CalendarItem.SelectedItem -> when (selectedCalendarItem) {
            is SelectedCalendarItem.SelectedTimeEntry -> selectedCalendarItem.editableTimeEntry.duration
            is SelectedCalendarItem.SelectedCalendarEvent -> selectedCalendarItem.calendarEvent.duration
        }
    }

val CalendarItem.isRunning: Boolean
    get() = this is CalendarItem.TimeEntry && this.timeEntry.duration == null ||
        (this is CalendarItem.SelectedItem && this.selectedCalendarItem is SelectedCalendarItem.SelectedTimeEntry &&
            this.selectedCalendarItem.editableTimeEntry.isRunning())

val SelectedCalendarItem.startTime: OffsetDateTime
    get() =
        when (this) {
            is SelectedCalendarItem.SelectedTimeEntry -> editableTimeEntry.startTime!!
            is SelectedCalendarItem.SelectedCalendarEvent -> calendarEvent.startTime
        }

val SelectedCalendarItem.endTime: OffsetDateTime?
    get() =
        when (this) {
            is SelectedCalendarItem.SelectedTimeEntry -> editableTimeEntry.startTime!!.maybePlus(editableTimeEntry.duration)
            is SelectedCalendarItem.SelectedCalendarEvent -> calendarEvent.startTime + calendarEvent.duration
        }

val SelectedCalendarItem.description: String
    get() = when (this) {
        is SelectedCalendarItem.SelectedTimeEntry -> editableTimeEntry.description
        is SelectedCalendarItem.SelectedCalendarEvent -> calendarEvent.description
    }

fun CalendarItem.toSelectedCalendarItem(): SelectedCalendarItem = when (this) {
    is CalendarItem.TimeEntry -> SelectedCalendarItem.SelectedTimeEntry(EditableTimeEntry.fromSingle(this.timeEntry))
    is CalendarItem.CalendarEvent -> SelectedCalendarItem.SelectedCalendarEvent(this.calendarEvent)
    is CalendarItem.SelectedItem -> this.selectedCalendarItem
}

@ExperimentalContracts
fun SelectedCalendarItem?.toEditableTimeEntry(): EditableTimeEntry {
    contract {
        returns() implies (this@toEditableTimeEntry is SelectedCalendarItem.SelectedTimeEntry)
    }

    return when (this) {
        null -> throw SelectedItemShouldNotBeNullException()
        is SelectedCalendarItem.SelectedCalendarEvent -> throw SelectedItemShouldBeATimeEntryException()
        is SelectedCalendarItem.SelectedTimeEntry -> editableTimeEntry
    }
}

fun SelectedCalendarItem?.toCalendarEvent() =
    when (this) {
        null -> throw SelectedItemShouldNotBeNullException()
        is SelectedCalendarItem.SelectedTimeEntry -> throw SelectedItemShouldBeACalendarEventException()
        is SelectedCalendarItem.SelectedCalendarEvent -> calendarEvent
    }