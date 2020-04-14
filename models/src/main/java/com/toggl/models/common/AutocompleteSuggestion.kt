package com.toggl.models.common

sealed class AutocompleteSuggestion {
    data class TimeEntry(val timeEntry: com.toggl.models.domain.TimeEntry) : AutocompleteSuggestion()
    data class Project(val project: com.toggl.models.domain.Project) : AutocompleteSuggestion()
    data class Task(val task: com.toggl.models.domain.Task) : AutocompleteSuggestion()
    data class Tag(val tag: com.toggl.models.domain.Tag) : AutocompleteSuggestion()
    data class CreateProject(val name: String) : AutocompleteSuggestion()
    data class CreateTag(val name: String) : AutocompleteSuggestion()
}
