package com.toggl.models.common

sealed class AutocompleteSuggestion {
    sealed class StartEditSuggestions : AutocompleteSuggestion() {
        data class TimeEntry(val timeEntry: com.toggl.models.domain.TimeEntry) : StartEditSuggestions()
        data class Project(val project: com.toggl.models.domain.Project) : StartEditSuggestions()
        data class Task(val task: com.toggl.models.domain.Task) : StartEditSuggestions()
        data class Tag(val tag: com.toggl.models.domain.Tag) : StartEditSuggestions()
        data class CreateProject(val name: String) : StartEditSuggestions()
        data class CreateTag(val name: String) : StartEditSuggestions()
    }

    sealed class ProjectSuggestions : AutocompleteSuggestion() {
        data class Workspace(val workspace: com.toggl.models.domain.Workspace) : ProjectSuggestions()
        data class Client(val client: com.toggl.models.domain.Client?) : ProjectSuggestions()
        data class CreateClient(val name: String) : ProjectSuggestions()
    }
}
