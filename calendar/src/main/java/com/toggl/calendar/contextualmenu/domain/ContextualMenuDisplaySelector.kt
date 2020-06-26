package com.toggl.calendar.contextualmenu.domain

import com.toggl.architecture.core.Selector
import com.toggl.calendar.common.domain.endTime
import com.toggl.calendar.common.domain.startTime
import com.toggl.common.feature.domain.ProjectViewModel
import com.toggl.common.feature.models.SelectedCalendarItem
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContextualMenuDisplaySelector @Inject constructor() : Selector<ContextualMenuState, ContextualMenuLabelsViewModel> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override suspend fun select(state: ContextualMenuState): ContextualMenuLabelsViewModel {
        return when (state.selectedItem) {
            is SelectedCalendarItem.SelectedTimeEntry -> state.createTimeEntryContextualMenu(state.selectedItem)
            is SelectedCalendarItem.SelectedCalendarEvent -> state.createCalendarEventContextualMenu(state.selectedItem)
        }
    }

    private fun ContextualMenuState.createTimeEntryContextualMenu(selectedItem: SelectedCalendarItem.SelectedTimeEntry): ContextualMenuLabelsViewModel.TimeEntryContextualMenu {
        val period = formatPeriod(selectedItem)
        val timeEntry = selectedItem.editableTimeEntry
        return ContextualMenuLabelsViewModel.TimeEntryContextualMenu(
            description = timeEntry.description,
            periodLabel = period,
            projectViewModel = getProjectViewModel(timeEntry.projectId)
        )
    }

    private fun ContextualMenuState.createCalendarEventContextualMenu(selectedItem: SelectedCalendarItem.SelectedCalendarEvent): ContextualMenuLabelsViewModel.CalendarEventContextualMenu {
        val period = formatPeriod(selectedItem)
        val calendarEvent = selectedItem.calendarEvent
        return ContextualMenuLabelsViewModel.CalendarEventContextualMenu(
            description = calendarEvent.description,
            periodLabel = period,
            calendarColor = calendarEvent.color,
            calendarName = getCalendarName(calendarEvent.calendarId)
        )
    }

    private fun ContextualMenuState.getCalendarName(calendarId: String): String? {
        return calendars.find { it.id == calendarId }?.name
    }

    private fun ContextualMenuState.getProjectViewModel(projectId: Long?): ProjectViewModel? =
        projects[projectId]?.let {
            ProjectViewModel(
                id = it.id,
                name = it.name,
                color = it.color,
                clientName = clients[it.clientId]?.name
            )
        }

    private fun formatPeriod(selectedItem: SelectedCalendarItem): String {
        val startTime = formatter.format(selectedItem.startTime)
        val endTime = formatter.format(selectedItem.endTime ?: OffsetDateTime.now())
        return "$startTime - $endTime"
    }
}