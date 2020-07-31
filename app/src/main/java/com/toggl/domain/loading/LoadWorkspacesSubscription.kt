package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.repository.interfaces.WorkspaceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class LoadWorkspacesSubscription(
    private val taskRepository: WorkspaceRepository,
    dispatcherProvider: DispatcherProvider
) : BaseLoadingSubscription(dispatcherProvider) {
    override fun subscribe(isUserLoggedIn: Boolean): Flow<LoadingAction> {
        val projects = if (isUserLoggedIn) taskRepository.loadWorkspaces()
        else flowOf(emptyList())
        return projects.map { LoadingAction.WorkspacesLoaded(it) }
    }
}
