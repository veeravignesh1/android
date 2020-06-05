package com.toggl.timer.startedit.ui.suggestions

import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.timer.log.domain.ProjectViewModel

sealed class SuggestionViewModel {
    data class TimeEntrySuggestion(
        val id: Long,
        val description: String,
        val projectViewModel: ProjectViewModel?,
        val taskName: String?,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : SuggestionViewModel()

    data class ProjectSuggestion(
        val id: Long,
        val projectViewModel: ProjectViewModel,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : SuggestionViewModel()

    data class TaskSuggestion(
        val id: Long,
        val taskName: String,
        val projectViewModel: ProjectViewModel,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : SuggestionViewModel()

    data class TagSuggestion(
        val id: Long,
        val tagName: String,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : SuggestionViewModel()

    data class CreateProject(
        val name: String,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : SuggestionViewModel()

    data class CreateTag(
        val name: String,
        val autocompleteSuggestion: AutocompleteSuggestion
    ) : SuggestionViewModel()
}