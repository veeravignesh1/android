package com.toggl.repository.interfaces

import com.toggl.models.domain.Workspace
import kotlinx.coroutines.flow.Flow

interface WorkspaceRepository {
    fun loadWorkspaces(): Flow<List<Workspace>>
    suspend fun workspacesCount(): Int
}
