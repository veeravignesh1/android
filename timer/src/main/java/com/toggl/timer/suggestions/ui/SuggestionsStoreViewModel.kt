package com.toggl.timer.suggestions.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.timer.suggestions.domain.SuggestionsAction
import com.toggl.timer.suggestions.domain.SuggestionsState

class SuggestionsStoreViewModel @ViewModelInject constructor(
    store: Store<SuggestionsState, SuggestionsAction>
) : ViewModel(), Store<SuggestionsState, SuggestionsAction> by store
