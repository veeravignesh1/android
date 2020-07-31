package com.toggl.calendar.datepicker.domain

import com.toggl.calendar.common.domain.CalendarState
import com.toggl.models.domain.UserPreferences
import java.time.OffsetDateTime

data class CalendarDatePickerState(
    val selectedDate: OffsetDateTime,
    val userPreferences: UserPreferences
) {
    companion object {
        fun fromCalendarState(calendarState: CalendarState) =
            CalendarDatePickerState(
                selectedDate = calendarState.localState.selectedDate,
                userPreferences = calendarState.userPreferences
            )

        fun toCalendarState(calendarState: CalendarState, datePickerState: CalendarDatePickerState) =
            calendarState.copy(
                localState = calendarState.localState.copy(
                    selectedDate = datePickerState.selectedDate
                )
            )
    }
}
