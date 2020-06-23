package com.toggl.calendar.contextualmenu.domain

import arrow.optics.optics
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.environment.services.calendar.Calendar
import com.toggl.models.domain.Client
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry

@optics
data class ContextualMenuState(
    val timeEntries: Map<Long, TimeEntry>,
    val projects: Map<Long, Project>,
    val clients: Map<Long, Client>,
    val calendars: List<Calendar>,
    val editableTimeEntry: EditableTimeEntry?,
    val selectedItem: SelectedCalendarItem
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            calendarState.localState.selectedItem?.let {
                ContextualMenuState(
                    calendarState.timeEntries,
                    calendarState.projects,
                    calendarState.clients,
                    calendarState.localState.calendars,
                    calendarState.editableTimeEntry,
                    calendarState.localState.selectedItem
                )
            }

        fun toCalendarState(calendarState: CalendarState, contextualMenuState: ContextualMenuState?) =
            contextualMenuState?.let {
                calendarState.copy(
                    timeEntries = contextualMenuState.timeEntries,
                    projects = contextualMenuState.projects,
                    clients = contextualMenuState.clients,
                    editableTimeEntry = contextualMenuState.editableTimeEntry,
                    localState = calendarState.localState.copy(
                        selectedItem = contextualMenuState.selectedItem
                    )
                )
            } ?: calendarState
    }
}