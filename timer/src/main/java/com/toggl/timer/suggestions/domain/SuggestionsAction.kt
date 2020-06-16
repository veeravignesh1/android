package com.toggl.timer.suggestions.domain

import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryActionHolder

sealed class SuggestionsAction {
    object LoadSuggestions : SuggestionsAction()
    data class SuggestionsLoaded(val suggestions: List<Suggestion>) : SuggestionsAction()
    data class SuggestionTapped(val suggestion: Suggestion) : SuggestionsAction()

    data class TimeEntryHandling(override val timeEntryAction: TimeEntryAction) : SuggestionsAction(), TimeEntryActionHolder

    companion object
}

fun SuggestionsAction.formatForDebug() =
    when (this) {
        SuggestionsAction.LoadSuggestions -> "Load suggestions"
        is SuggestionsAction.SuggestionsLoaded -> "Suggestions loaded $suggestions"
        is SuggestionsAction.SuggestionTapped -> "Selected suggestion $suggestion"
        is SuggestionsAction.TimeEntryHandling -> "Time entry action $timeEntryAction"
    }