package com.toggl.calendar.contextualmenu.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.calendar.contextualmenu.domain.ContextualMenuAction
import com.toggl.calendar.contextualmenu.domain.ContextualMenuState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

class ContextualMenuStoreViewModel @ViewModelInject constructor(
    store: Store<ContextualMenuState, ContextualMenuAction>
) : ViewModel(), Store<ContextualMenuState, ContextualMenuAction> by store {

    @ExperimentalCoroutinesApi
    val slideOffsetInPixels = MutableStateFlow(0)
}
