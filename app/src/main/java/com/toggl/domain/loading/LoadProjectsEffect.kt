package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.ProjectRepository
import kotlinx.coroutines.withContext

class LoadProjectsEffect(
    private val repository: ProjectRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<LoadingAction> {
    override suspend fun execute(): LoadingAction? =
        withContext(dispatcherProvider.computation) {
            LoadingAction.ProjectsLoaded(repository.loadProjects())
        }
}
