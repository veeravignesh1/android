package com.toggl.timer.suggestions.domain

import com.toggl.timer.log.domain.ProjectViewModel

data class SuggestionViewModel(
    val id: String,
    val description: String,
    val project: ProjectViewModel?,
    val suggestion: Suggestion
)
