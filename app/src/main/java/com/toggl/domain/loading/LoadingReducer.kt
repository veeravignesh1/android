package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.effects
import com.toggl.architecture.extensions.noEffect
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.repository.interfaces.WorkspaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingReducer @Inject constructor(
    private val timeEntryRepository: TimeEntryRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<LoadingState, LoadingAction> {

    override fun reduce(
        state: SettableValue<LoadingState>,
        action: LoadingAction
    ): List<Effect<LoadingAction>> =
        when (action) {
            LoadingAction.StartLoading -> effects(
                LoadTimeEntriesEffect(timeEntryRepository, dispatcherProvider),
                LoadWorkspacesEffect(workspaceRepository, dispatcherProvider)
            )
            is LoadingAction.TimeEntriesLoaded -> {
                state.value = state.value.copy(timeEntries = action.timeEntries)
                noEffect()
            }
            is LoadingAction.WorkspacesLoaded -> {
                state.value = state.value.copy(workspaces = action.workspaces)
                noEffect()
            }
        }
}