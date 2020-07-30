package com.toggl.reports.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.reports.domain.ReportsAction
import com.toggl.reports.domain.ReportsState

class ReportsStoreViewModel @ViewModelInject constructor(
    store: Store<ReportsState, ReportsAction>
) : ViewModel(), Store<ReportsState, ReportsAction> by store
