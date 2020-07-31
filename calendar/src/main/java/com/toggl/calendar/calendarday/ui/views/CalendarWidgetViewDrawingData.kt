package com.toggl.calendar.calendarday.ui.views

import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.calendar.common.domain.endTime
import com.toggl.calendar.common.domain.startTime
import java.time.OffsetDateTime

class CalendarWidgetViewDrawingData(
    sourceItems: List<CalendarItem> = emptyList()
) {
    val nonSelectedCalendarItemsToDraw: List<CalendarItem>
    val itemsStartAndEndTimes: List<OffsetDateTime>
    val selectedCalendarItemToDraw: CalendarItem?

    init {
        val selectedItemIndex = sourceItems.indexOfFirst { it is CalendarItem.SelectedItem }
        nonSelectedCalendarItemsToDraw = sourceItems.filterIndexed { index, _ -> index != selectedItemIndex }
        selectedCalendarItemToDraw = if (selectedItemIndex != -1) sourceItems[selectedItemIndex] else null
        val startTimes = sourceItems.map(CalendarItem::startTime)
        val endTimes = sourceItems.mapNotNull(CalendarItem::endTime)
        itemsStartAndEndTimes = (startTimes + endTimes).distinct()
    }
}