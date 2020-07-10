package com.toggl.calendar.datepicker.domain

import com.toggl.architecture.core.Selector
import com.toggl.calendar.datepicker.ui.DatePickerViewModel

class DatePickerSelector : Selector<CalendarDatePickerState, DatePickerViewModel> {
    override suspend fun select(state: CalendarDatePickerState): DatePickerViewModel {
        return DatePickerViewModel(emptyList(), emptyList(), 0)
    }
}