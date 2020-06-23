package com.toggl.timer.startedit.ui.autocomplete

import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.common.feature.domain.ProjectViewModel

sealed class AutocompleteSuggestionViewModel {
    data class TimeEntryAutocompleteSuggestion(
        val id: Long,
        val description: String,
        val projectViewModel: ProjectViewModel?,
        val taskName: String?,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : AutocompleteSuggestionViewModel()

    data class ProjectAutocompleteSuggestion(
        val id: Long,
        val projectViewModel: ProjectViewModel,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : AutocompleteSuggestionViewModel()

    data class TaskAutocompleteSuggestion(
        val id: Long,
        val taskName: String,
        val projectViewModel: ProjectViewModel,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : AutocompleteSuggestionViewModel()

    data class TagAutocompleteSuggestion(
        val id: Long,
        val tagName: String,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : AutocompleteSuggestionViewModel()

    data class CreateProject(
        val name: String,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : AutocompleteSuggestionViewModel()

    data class CreateTag(
        val name: String,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : AutocompleteSuggestionViewModel()
}