package com.toggl.timer.suggestions.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.noEffect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuggestionsReducer @Inject constructor() : Reducer<SuggestionsState, SuggestionsAction> {

    override fun reduce(
        state: MutableValue<SuggestionsState>,
        action: SuggestionsAction
    ): List<Effect<SuggestionsAction>> =
        when (action) {
            is SuggestionsAction.TimeEntryHandling -> noEffect()
        }
}
