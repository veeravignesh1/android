package com.toggl.calendar.contextualmenu.domain

import arrow.optics.optics
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.models.domain.TimeEntry

@optics
data class ContextualMenuState(
    val timeEntries: Map<Long, TimeEntry>,
    val selectedItem: SelectedCalendarItem?
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            ContextualMenuState(
                calendarState.timeEntries,
                calendarState.localState.selectedItem
            )

        fun toCalendarState(calendarState: CalendarState, contextualMenuState: ContextualMenuState) =
            calendarState.copy(
                timeEntries = contextualMenuState.timeEntries,
                localState = calendarState.localState.copy(
                    selectedItem = contextualMenuState.selectedItem
                )
            )
    }
}