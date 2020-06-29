package com.toggl.timer.suggestions.ui

import com.toggl.architecture.core.Selector
import com.toggl.timer.suggestions.domain.SuggestionViewModel
import com.toggl.timer.suggestions.domain.SuggestionsState
import com.toggl.timer.suggestions.domain.toSuggestionViewModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SuggestionsLogSelector @Inject constructor() : Selector<SuggestionsState, List<SuggestionViewModel>> {
    override suspend fun select(state: SuggestionsState): List<SuggestionViewModel> {
        val projects = state.projects
        val clients = state.clients
        val suggestions = state.suggestions

        return suggestions.map { it.toSuggestionViewModel(projects, clients) }
    }
}