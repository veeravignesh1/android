package com.toggl.domain.loading

import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.extensions.effects
import com.toggl.repository.interfaces.ClientRepository
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.repository.interfaces.TagRepository
import com.toggl.repository.interfaces.TaskRepository
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.repository.interfaces.WorkspaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingReducer @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val clientRepository: ClientRepository,
    private val timeEntryRepository: TimeEntryRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val tagsRepository: TagRepository,
    private val taskRepository: TaskRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<LoadingState, LoadingAction> {

    override fun reduce(
        state: MutableValue<LoadingState>,
        action: LoadingAction
    ): List<Effect<LoadingAction>> =
        when (action) {
            LoadingAction.StartLoading -> effects(
                LoadWorkspacesEffect(workspaceRepository, dispatcherProvider),
                LoadProjectsEffect(projectRepository, dispatcherProvider),
                LoadClientsEffect(clientRepository, dispatcherProvider),
                LoadTagsEffect(tagsRepository, dispatcherProvider),
                LoadTasksEffect(taskRepository, dispatcherProvider),
                LoadTimeEntriesEffect(timeEntryRepository, dispatcherProvider)
            )
            is LoadingAction.TimeEntriesLoaded -> state.mutateWithoutEffects { copy(timeEntries = action.timeEntries) }
            is LoadingAction.WorkspacesLoaded -> state.mutateWithoutEffects { copy(workspaces = action.workspaces) }
            is LoadingAction.ProjectsLoaded -> state.mutateWithoutEffects { copy(projects = action.projects) }
            is LoadingAction.ClientsLoaded -> state.mutateWithoutEffects { copy(clients = action.clients) }
            is LoadingAction.TagsLoaded -> state.mutateWithoutEffects { copy(tags = action.tags) }
            is LoadingAction.TasksLoaded -> state.mutateWithoutEffects { copy(tasks = action.tasks) }
        }
}