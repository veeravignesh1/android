package com.toggl.calendar.calendarday.ui.views

import com.toggl.calendar.common.domain.CalendarItem

class CalendarWidgetViewDrawingData(
    sourceItems: List<CalendarItem> = emptyList()
) {
    val nonSelectedCalendarItemsToDraw: List<CalendarItem>
    val selectedCalendarItemToDraw: CalendarItem?

    init {
        val selectedItemIndex = sourceItems.indexOfFirst { it is CalendarItem.SelectedItem }
        nonSelectedCalendarItemsToDraw = sourceItems.filterIndexed { index, _ -> index != selectedItemIndex }
        selectedCalendarItemToDraw = if (selectedItemIndex != -1) sourceItems[selectedItemIndex] else null
    }
}