package com.toggl.domain.loading

import com.toggl.repository.interfaces.ProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoadProjectsSubscriptions @Inject constructor(
    private val projectRepository: ProjectRepository
) : BaseLoadingSubscription() {
    override fun subscribe(isUserLoggedIn: Boolean): Flow<LoadingAction> {
        val projects = if (isUserLoggedIn) projectRepository.loadProjects()
        else flowOf(emptyList())
        return projects.map { LoadingAction.ProjectsLoaded(it) }
    }
}