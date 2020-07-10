package com.toggl.calendar.datepicker.domain

import arrow.optics.optics
import com.toggl.calendar.common.domain.CalendarState
import java.time.OffsetDateTime

@optics
data class CalendarDatePickerState(
    val selectedDate: OffsetDateTime,
    val availableDates: List<OffsetDateTime>,
    val visibleDates: List<OffsetDateTime>
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            CalendarDatePickerState(
                selectedDate = calendarState.localState.selectedDate,
                availableDates = calendarState.localState.availableDates,
                visibleDates = calendarState.localState.visibleDates
            )

        fun toCalendarState(calendarState: CalendarState, datePickerState: CalendarDatePickerState) =
            calendarState.copy(
                localState = calendarState.localState.copy(
                    selectedDate = datePickerState.selectedDate,
                    availableDates = datePickerState.availableDates,
                    visibleDates = datePickerState.visibleDates
                )
            )
    }
}