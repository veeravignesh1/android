package com.toggl.calendar.contextualmenu.domain

import arrow.optics.optics
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.common.feature.navigation.BackStack
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.letRouteParamIfAny
import com.toggl.common.feature.navigation.setRouteParam
import com.toggl.models.domain.TimeEntry

@optics
data class ContextualMenuState(
    val timeEntries: Map<Long, TimeEntry>,
    val backStack: BackStack,
    val selectedItem: SelectedCalendarItem
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            calendarState.backStack.letRouteParamIfAny<SelectedCalendarItem, ContextualMenuState> {
                ContextualMenuState(
                    calendarState.timeEntries,
                    calendarState.backStack,
                    it
                )
            }

        fun toCalendarState(calendarState: CalendarState, contextualMenuState: ContextualMenuState?) =
            contextualMenuState?.let {
                calendarState.copy(
                    timeEntries = contextualMenuState.timeEntries,
                    backStack = contextualMenuState.backStack.setRouteParam { Route.ContextualMenu(contextualMenuState.selectedItem) }
                )
            } ?: calendarState
    }
}