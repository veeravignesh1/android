package com.toggl.calendar.datepicker.domain

import java.time.OffsetDateTime

sealed class CalendarDatePickerAction {
    object OnViewAppeared : CalendarDatePickerAction()
    data class DatesLoaded(
        val availableDates: List<OffsetDateTime>,
        val visibleDates: List<OffsetDateTime>
    ) : CalendarDatePickerAction()
}

fun CalendarDatePickerAction.formatForDebug() =
    when (this) {
        is CalendarDatePickerAction.OnViewAppeared -> "Date picker appeared"
        is CalendarDatePickerAction.DatesLoaded -> "Calendar date picker dates loaded"
    }
