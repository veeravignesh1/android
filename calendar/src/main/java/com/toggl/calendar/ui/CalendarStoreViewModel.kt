package com.toggl.calendar.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.calendar.domain.CalendarAction
import com.toggl.calendar.domain.CalendarState
import javax.inject.Inject

class CalendarStoreViewModel @Inject constructor(
    store: Store<CalendarState, CalendarAction>
) : ViewModel(), Store<CalendarState, CalendarAction> by store