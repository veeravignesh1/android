package com.toggl.repository.interfaces

import com.toggl.models.domain.Workspace

interface WorkspaceRepository {
    suspend fun loadWorkspaces(): List<Workspace>
}
