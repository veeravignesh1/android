package com.toggl.timer.log.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.timer.log.domain.TimeEntriesLogAction
import com.toggl.timer.log.domain.TimeEntriesLogState

class TimeEntriesLogStoreViewModel @ViewModelInject constructor(
    store: Store<TimeEntriesLogState, TimeEntriesLogAction>
) : ViewModel(), Store<TimeEntriesLogState, TimeEntriesLogAction> by store
