package com.toggl.timer.startedit.domain

import com.toggl.architecture.core.Selector
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.log.domain.getProjectViewModelFor
import com.toggl.timer.log.domain.toProjectViewModel
import com.toggl.timer.startedit.ui.suggestions.SuggestionViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuggestionsSelector @Inject constructor() : Selector<StartEditState, List<SuggestionViewModel>> {
    override suspend fun select(state: StartEditState): List<SuggestionViewModel> {
        val suggestions = state.autocompleteSuggestions
        val projects = state.projects
        val clients = state.clients
        val tasks = state.tasks

        return suggestions.map {
            when (it) {
                is AutocompleteSuggestion.TimeEntry ->
                    SuggestionViewModel.TimeEntrySuggestion(
                        it.timeEntry.id,
                        it.timeEntry.description,
                        projects.getProjectViewModelFor(it.timeEntry, clients),
                        tasks.getTaskNameFor(it.timeEntry),
                        it
                    )
                is AutocompleteSuggestion.Project ->
                    SuggestionViewModel.ProjectSuggestion(
                        it.project.id,
                        it.project.toProjectViewModel(clients),
                        it
                    )
                is AutocompleteSuggestion.Task ->
                    SuggestionViewModel.TaskSuggestion(
                        it.task.id,
                        it.task.name,
                        projects[it.task.projectId]?.toProjectViewModel(clients)
                            ?: throw throw IllegalStateException("Project not found in state for task ${it.task.projectId}"),
                        it
                    )
                is AutocompleteSuggestion.Tag ->
                    SuggestionViewModel.TagSuggestion(
                        it.tag.id,
                        it.tag.name,
                        it
                    )
                is AutocompleteSuggestion.CreateProject -> SuggestionViewModel.CreateProject(it.name, it)
                is AutocompleteSuggestion.CreateTag -> SuggestionViewModel.CreateTag(it.name, it)
            }
        }
    }
}

fun Map<Long, Task>.getTaskNameFor(timeEntry: TimeEntry): String? {
    val taskId = timeEntry.taskId
    return if (taskId == null) null
    else this[taskId]?.name
}