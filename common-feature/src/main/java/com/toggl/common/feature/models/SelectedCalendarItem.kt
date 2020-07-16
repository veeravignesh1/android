package com.toggl.common.feature.models

import com.toggl.common.feature.services.calendar.CalendarEvent
import com.toggl.models.domain.EditableTimeEntry

sealed class SelectedCalendarItem {
    data class SelectedTimeEntry(val editableTimeEntry: EditableTimeEntry) : SelectedCalendarItem()
    data class SelectedCalendarEvent(val calendarEvent: CalendarEvent) : SelectedCalendarItem()
}
