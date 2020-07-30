package com.toggl.timer.suggestions.domain

import com.toggl.architecture.core.Effect
import com.toggl.timer.suggestions.domain.SuggestionsAction.SuggestionsLoaded

class LoadSuggestionEffect(
    private val suggestionProvider: SuggestionProvider,
    private val suggestionsState: SuggestionsState
) : Effect<SuggestionsLoaded> {
    override suspend fun execute(): SuggestionsLoaded? =
        SuggestionsLoaded(suggestionProvider.getSuggestions(suggestionsState))
}
