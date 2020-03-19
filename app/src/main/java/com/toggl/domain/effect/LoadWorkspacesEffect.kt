package com.toggl.domain.effect

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.domain.AppAction
import com.toggl.repository.interfaces.WorkspaceRepository
import kotlinx.coroutines.withContext

class LoadWorkspacesEffect(
    private val repository: WorkspaceRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<AppAction> {
    override suspend fun execute(): AppAction? =
        withContext(dispatcherProvider.computation) {
            AppAction.WorkspacesLoaded(repository.loadWorkspaces())
        }
}
