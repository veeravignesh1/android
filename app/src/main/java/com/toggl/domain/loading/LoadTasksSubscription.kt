package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.repository.interfaces.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class LoadTasksSubscription(
    private val taskRepository: TaskRepository,
    dispatcherProvider: DispatcherProvider
) : BaseLoadingSubscription(dispatcherProvider) {
    override fun subscribe(isUserLoggedIn: Boolean): Flow<LoadingAction> {
        val projects = if (isUserLoggedIn) taskRepository.loadTasks()
        else flowOf(emptyList())
        return projects.map { LoadingAction.TasksLoaded(it) }
    }
}
