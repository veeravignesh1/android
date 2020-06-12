package com.toggl.calendar.calendarday.domain

import com.toggl.architecture.core.Selector
import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.common.extensions.maybePlus
import com.toggl.common.feature.timeentry.extensions.wasNotYetPersisted
import com.toggl.common.feature.timeentry.extensions.wasPersisted
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarItemsSelector @Inject constructor(
    private val calendarLayoutCalculator: CalendarLayoutCalculator
) : Selector<CalendarDayState, List<CalendarItem>> {

    override suspend fun select(state: CalendarDayState): List<CalendarItem> {
        val localDate = state.date.toLocalDate()
        val projects = state.projects
        fun isOnDate(startTime: OffsetDateTime, endTime: OffsetDateTime?) =
            startTime.toLocalDate() == localDate && (endTime == null || endTime.toLocalDate() == localDate)

        fun createReplaceSelectedTimeEntryItemIfNeededMapper(selectedCalendarItem: SelectedCalendarItem?): (CalendarItem.TimeEntry) -> CalendarItem {
            if (selectedCalendarItem != null &&
                selectedCalendarItem is SelectedCalendarItem.SelectedTimeEntry &&
                selectedCalendarItem.editableTimeEntry.wasPersisted()
            ) {
                val selectedItemId = selectedCalendarItem.editableTimeEntry.ids.first()
                return {
                    if (selectedItemId != it.timeEntry.id) it
                    else CalendarItem.SelectedItem(selectedCalendarItem, color = projects[it.timeEntry.projectId]?.color)
                }
            }
            return { it }
        }

        fun createReplaceSelectedCalendarItemIfNeededMapper(selectedCalendarItem: SelectedCalendarItem?): (CalendarItem.CalendarEvent) -> CalendarItem {
            if (selectedCalendarItem != null && selectedCalendarItem is SelectedCalendarItem.SelectedCalendarEvent) {
                val selectedItemId = selectedCalendarItem.calendarEvent.id
                return {
                    if (selectedItemId != it.calendarEvent.id) it
                    else CalendarItem.SelectedItem(selectedCalendarItem, color = it.calendarEvent.color)
                }
            }
            return { it }
        }

        val filteredTimeEntries =
            state.timeEntries.filterValues { isOnDate(it.startTime, it.startTime.maybePlus(it.duration)) }
        val filteredEvents =
            state.events.filterValues { isOnDate(it.startTime, it.startTime + it.duration) }

        val selectedCalendarItem = state.selectedItem
        val replaceSelectedTimeEntryIfNeeded = createReplaceSelectedTimeEntryItemIfNeededMapper(selectedCalendarItem)
        val replaceSelectedCalendarEventIfNeeded = createReplaceSelectedCalendarItemIfNeededMapper(selectedCalendarItem)

        val timeEntries = filteredTimeEntries.values
            .asSequence()
            .map { CalendarItem.TimeEntry(it, projectColor = projects[it.projectId]?.color) }
            .map(replaceSelectedTimeEntryIfNeeded)

        val calendarEvents = filteredEvents.values
            .asSequence()
            .map { CalendarItem.CalendarEvent(it) }
            .map(replaceSelectedCalendarEventIfNeeded)

        val newSelectedItem =
            if (selectedCalendarItem is SelectedCalendarItem.SelectedTimeEntry && selectedCalendarItem.editableTimeEntry.wasNotYetPersisted()) {
                sequenceOf(
                    CalendarItem.SelectedItem(
                        selectedCalendarItem,
                        projects[selectedCalendarItem.editableTimeEntry.projectId]?.color
                    )
                )
            } else emptySequence()

        return (timeEntries + calendarEvents + newSelectedItem)
            .toList()
            .run(calendarLayoutCalculator::calculateLayoutAttributes)
    }
}
