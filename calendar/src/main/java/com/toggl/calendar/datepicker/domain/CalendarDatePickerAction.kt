package com.toggl.calendar.datepicker.domain

import com.toggl.models.common.SwipeDirection
import java.time.OffsetDateTime

sealed class CalendarDatePickerAction {
    object OnViewAppeared : CalendarDatePickerAction()
    data class DatesLoaded(
        val availableDates: List<OffsetDateTime>,
        val visibleDates: List<OffsetDateTime>
    ) : CalendarDatePickerAction()
    data class DaySelected(val day: OffsetDateTime) : CalendarDatePickerAction()
    data class DaySwiped(val direction: SwipeDirection) : CalendarDatePickerAction()
    data class WeekStripeSwiped(val direction: SwipeDirection) : CalendarDatePickerAction()
}

fun CalendarDatePickerAction.formatForDebug() =
    when (this) {
        is CalendarDatePickerAction.OnViewAppeared -> "Date picker appeared"
        is CalendarDatePickerAction.DatesLoaded -> "Calendar date picker dates loaded"
        is CalendarDatePickerAction.DaySelected -> "Calendar date picker day selected: $day"
        is CalendarDatePickerAction.DaySwiped -> "Calendar date picker day swiped: $direction"
        is CalendarDatePickerAction.WeekStripeSwiped -> "Week stripe swiped $direction"
    }
