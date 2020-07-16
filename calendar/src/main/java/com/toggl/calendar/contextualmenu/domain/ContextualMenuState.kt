package com.toggl.calendar.contextualmenu.domain

import arrow.optics.optics
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.common.feature.navigation.BackStack
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.getRouteParam
import com.toggl.common.feature.navigation.setRouteParam
import com.toggl.common.feature.services.calendar.Calendar
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry

@optics
data class ContextualMenuState(
    val timeEntries: Map<Long, TimeEntry>,
    val projects: Map<Long, Project>,
    val clients: Map<Long, Client>,
    val calendars: List<Calendar>,
    val backStack: BackStack,
    val selectedItem: SelectedCalendarItem
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            calendarState.backStack.getRouteParam<SelectedCalendarItem>()?.let {
                ContextualMenuState(
                    calendarState.timeEntries,
                    calendarState.projects,
                    calendarState.clients,
                    calendarState.localState.calendars,
                    calendarState.backStack,
                    it
                )
            }

        fun toCalendarState(calendarState: CalendarState, contextualMenuState: ContextualMenuState?) =
            contextualMenuState?.let {
                calendarState.copy(
                    timeEntries = contextualMenuState.timeEntries,
                    projects = contextualMenuState.projects,
                    clients = contextualMenuState.clients,
                    backStack = contextualMenuState.backStack.setRouteParam {
                        Route.ContextualMenu((contextualMenuState.selectedItem))
                    }
                )
            } ?: calendarState
    }
}