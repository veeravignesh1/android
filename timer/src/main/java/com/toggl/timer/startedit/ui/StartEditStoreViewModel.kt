package com.toggl.timer.startedit.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.timer.startedit.domain.StartEditAction
import com.toggl.timer.startedit.domain.StartEditState

class StartEditStoreViewModel @ViewModelInject constructor(
    store: Store<StartEditState, StartEditAction>
) : ViewModel(), Store<StartEditState, StartEditAction> by store
