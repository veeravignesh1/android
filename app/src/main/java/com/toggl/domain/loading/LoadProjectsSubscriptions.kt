package com.toggl.domain.loading

import com.toggl.architecture.Loadable
import com.toggl.architecture.core.Subscription
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.repository.interfaces.ProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoadProjectsSubscriptions @Inject constructor(
    private val projectRepository: ProjectRepository
) : Subscription<AppState, AppAction> {
    override fun subscribe(state: Flow<AppState>): Flow<AppAction> =
        state.map { it.user is Loadable.Loaded }
            .distinctUntilChanged()
            .flatMapLatest { isLoggedIn ->
                if (isLoggedIn) {
                    projectRepository.loadProjects()
                        .map { AppAction.Loading(LoadingAction.ProjectsLoaded(it)) }
                } else flowOf(AppAction.Loading(LoadingAction.ProjectsLoaded(emptyList())))
            }
}