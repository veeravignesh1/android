package com.toggl.timer.suggestions.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.timer.suggestions.domain.SuggestionsAction
import com.toggl.timer.suggestions.domain.SuggestionsState
import javax.inject.Inject

class SuggestionsStoreViewModel @Inject constructor(
    store: Store<SuggestionsState, SuggestionsAction>
) : ViewModel(), Store<SuggestionsState, SuggestionsAction> by store