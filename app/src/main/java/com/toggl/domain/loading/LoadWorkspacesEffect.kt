package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.WorkspaceRepository
import kotlinx.coroutines.withContext

class LoadWorkspacesEffect(
    private val repository: WorkspaceRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<LoadingAction> {
    override suspend fun execute(): LoadingAction? =
        withContext(dispatcherProvider.io) {
            LoadingAction.WorkspacesLoaded(repository.loadWorkspaces())
        }
}
