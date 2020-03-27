package com.toggl.domain.loading

import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.Workspace

data class LoadingState(
    val workspaces: Collection<Workspace>,
    val projects: Collection<Project>,
    val timeEntries: Collection<TimeEntry>
)