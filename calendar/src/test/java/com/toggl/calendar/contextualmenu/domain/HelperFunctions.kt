package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.createCalendarEvent
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.TimeEntry

fun createInitialState(
    timeEntries: List<TimeEntry> = emptyList(),
    editableTimeEntry: EditableTimeEntry? = null,
    selectedItem: SelectedCalendarItem = SelectedCalendarItem.SelectedCalendarEvent(calendarEvent = createCalendarEvent())
) = ContextualMenuState(
    timeEntries.associateBy { it.id },
    editableTimeEntry,
    selectedItem
)