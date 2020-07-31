package com.toggl.timer.running.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.timer.running.domain.RunningTimeEntryAction
import com.toggl.timer.running.domain.RunningTimeEntryState

class RunningTimeEntryStoreViewModel @ViewModelInject constructor(
    store: Store<RunningTimeEntryState, RunningTimeEntryAction>
) : ViewModel(), Store<RunningTimeEntryState, RunningTimeEntryAction> by store