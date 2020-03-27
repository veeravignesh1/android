package com.toggl.domain.loading

import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.Workspace

sealed class LoadingAction {
    object StartLoading : LoadingAction()
    data class WorkspacesLoaded(val workspaces: List<Workspace>) : LoadingAction()
    data class TimeEntriesLoaded(val timeEntries: List<TimeEntry>) : LoadingAction()
}

fun LoadingAction.formatForDebug() =
    when (this) {
        LoadingAction.StartLoading -> "Initial load of entities"
        is LoadingAction.WorkspacesLoaded -> "Workspaces Loaded"
        is LoadingAction.TimeEntriesLoaded -> "Time Entries loaded"
    }
