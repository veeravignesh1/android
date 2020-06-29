package com.toggl.calendar.datepicker.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.calendar.datepicker.domain.CalendarDatePickerAction
import com.toggl.calendar.datepicker.domain.CalendarDatePickerState

class CalendarDatePickerStoreViewModel @ViewModelInject constructor(
    store: Store<CalendarDatePickerState, CalendarDatePickerAction>
) : ViewModel(), Store<CalendarDatePickerState, CalendarDatePickerAction> by store