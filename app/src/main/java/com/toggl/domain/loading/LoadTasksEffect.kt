package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.TaskRepository
import kotlinx.coroutines.withContext

class LoadTasksEffect(
    private val repository: TaskRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<LoadingAction> {
    override suspend fun execute(): LoadingAction? =
        withContext(dispatcherProvider.io) {
            LoadingAction.TasksLoaded(repository.loadTasks())
        }
}
