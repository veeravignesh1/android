package com.toggl.timer.startedit.domain

import com.toggl.architecture.core.Selector
import com.toggl.models.common.AutocompleteSuggestion.StartEditSuggestions
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.log.domain.getProjectViewModelFor
import com.toggl.timer.log.domain.toProjectViewModel
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class AutocompleteSuggestionsSelector @Inject constructor() : Selector<StartEditState, List<AutocompleteSuggestionViewModel>> {
    override suspend fun select(state: StartEditState): List<AutocompleteSuggestionViewModel> {
        val suggestions = state.autocompleteSuggestions
        val projects = state.projects
        val clients = state.clients
        val tasks = state.tasks

        return suggestions.map {
            when (it) {
                is StartEditSuggestions.TimeEntry ->
                    AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion(
                        it.timeEntry.id,
                        it.timeEntry.description,
                        projects.getProjectViewModelFor(it.timeEntry, clients),
                        tasks.getTaskNameFor(it.timeEntry),
                        it
                    )
                is StartEditSuggestions.Project ->
                    AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion(
                        it.project.id,
                        it.project.toProjectViewModel(clients),
                        it
                    )
                is StartEditSuggestions.Task ->
                    AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion(
                        it.task.id,
                        it.task.name,
                        projects[it.task.projectId]?.toProjectViewModel(clients)
                            ?: throw throw IllegalStateException("Project not found in state for task ${it.task.projectId}"),
                        it
                    )
                is StartEditSuggestions.Tag ->
                    AutocompleteSuggestionViewModel.TagAutocompleteSuggestion(
                        it.tag.id,
                        it.tag.name,
                        it
                    )
                is StartEditSuggestions.CreateProject -> AutocompleteSuggestionViewModel.CreateProject(it.name, it)
                is StartEditSuggestions.CreateTag -> AutocompleteSuggestionViewModel.CreateTag(it.name, it)
            }
        }
    }
}

fun Map<Long, Task>.getTaskNameFor(timeEntry: TimeEntry): String? {
    val taskId = timeEntry.taskId
    return if (taskId == null) null
    else this[taskId]?.name
}
