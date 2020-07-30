package com.toggl.timer.suggestions.domain

interface SuggestionProvider {
    suspend fun getSuggestions(suggestionsState: SuggestionsState): List<Suggestion>
}

internal class ComposeSuggestionProvider(
    private val maxNumberOfSuggestions: Int,
    vararg providers: SuggestionProvider
) : SuggestionProvider {

    private val providers = providers.toList()

    override suspend fun getSuggestions(suggestionsState: SuggestionsState): List<Suggestion> =
        providers.flatMap { it.getSuggestions(suggestionsState) }.take(maxNumberOfSuggestions)
}
