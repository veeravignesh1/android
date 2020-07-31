package com.toggl.calendar.calendarday.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.calendar.calendarday.domain.CalendarDayAction
import com.toggl.calendar.calendarday.domain.CalendarDayState
import kotlinx.coroutines.flow.MutableStateFlow

class CalendarDayStoreViewModel @ViewModelInject constructor(
    store: Store<CalendarDayState, CalendarDayAction>
) : ViewModel(), Store<CalendarDayState, CalendarDayAction> by store {
    val scrollOffset = MutableStateFlow(0)
    val hourHeight = MutableStateFlow(0f)
}
