package com.toggl.reports.domain

import com.toggl.models.domain.Workspace

data class ReportsState(
    val workspaces: Map<Long, Workspace>
)
