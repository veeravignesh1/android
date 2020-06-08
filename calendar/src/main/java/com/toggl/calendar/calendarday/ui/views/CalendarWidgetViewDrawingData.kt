package com.toggl.calendar.calendarday.ui.views

import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.calendar.common.domain.SelectedCalendarItem

class CalendarWidgetViewDrawingData(
    sourceItems: List<CalendarItem> = emptyList(),
    private val selectedCalendarItem: SelectedCalendarItem? = null
) {
    val nonSelectedCalendarItemsToDraw: List<CalendarItem>
    val selectedCalendarItemToDraw: CalendarItem?

    init {
        val selectedItemIndex = sourceItems.indexOfFirst { it.isSelectedItem(selectedCalendarItem) }
        nonSelectedCalendarItemsToDraw = sourceItems.filterIndexed { index, _ -> index != selectedItemIndex }
        selectedCalendarItemToDraw = if (selectedItemIndex != -1) sourceItems[selectedItemIndex] else null
    }
}

private fun CalendarItem.isSelectedItem(selectedItem: SelectedCalendarItem?): Boolean {
    return when (selectedItem) {
        null -> false
        is SelectedCalendarItem.SelectedTimeEntry -> this.isSelectedTimeEntry(selectedItem)
        is SelectedCalendarItem.SelectedCalendarEvent -> this.isSelectedCalendarEvent(selectedItem)
    }
}

private fun CalendarItem.isSelectedCalendarEvent(selectedItem: SelectedCalendarItem.SelectedCalendarEvent): Boolean {
    return this is CalendarItem.CalendarEvent && this.calendarEvent == selectedItem.calendarEvent
}

private fun CalendarItem.isSelectedTimeEntry(selectedItem: SelectedCalendarItem.SelectedTimeEntry): Boolean {
    return this is CalendarItem.TimeEntry &&
        (this.timeEntry.id == selectedItem.editableTimeEntry.ids.first() ||
            this.timeEntry.id == 0L && selectedItem.editableTimeEntry.ids.isEmpty())
}