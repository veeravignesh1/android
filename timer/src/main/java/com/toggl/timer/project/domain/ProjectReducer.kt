package com.toggl.timer.project.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.noEffect
import com.toggl.repository.interfaces.ProjectRepository
import javax.inject.Inject

class ProjectReducer @Inject constructor(
    private val repository: ProjectRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<ProjectState, ProjectAction> {

    override fun reduce(
        state: SettableValue<ProjectState>,
        action: ProjectAction
    ): List<Effect<ProjectAction>> =
        when (action) {
            is ProjectAction.NameEntered -> noEffect()
        }
}