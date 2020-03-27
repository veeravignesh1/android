package com.toggl.domain.loading

import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.Workspace

sealed class LoadingAction {
    object StartLoading : LoadingAction()
    data class WorkspacesLoaded(val workspaces: List<Workspace>) : LoadingAction()
    data class ProjectsLoaded(val projects: List<Project>) : LoadingAction()
    data class TimeEntriesLoaded(val timeEntries: List<TimeEntry>) : LoadingAction()
}

fun LoadingAction.formatForDebug() =
    when (this) {
        LoadingAction.StartLoading -> "Entities started loading"
        is LoadingAction.WorkspacesLoaded -> "Loaded ${workspaces.size} Workspaces"
        is LoadingAction.TimeEntriesLoaded -> "Loaded ${timeEntries.size} Time Entries"
        is LoadingAction.ProjectsLoaded -> "Loaded ${projects.size} projects"
    }
