package com.toggl.timer.project.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.repository.interfaces.ProjectRepository
import javax.inject.Inject

class ProjectReducer @Inject constructor(
    private val repository: ProjectRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<ProjectState, ProjectAction> {

    override fun reduce(
        state: MutableValue<ProjectState>,
        action: ProjectAction
    ): List<Effect<ProjectAction>> =
        when (action) {
            is ProjectAction.NameEntered -> state.mutateWithoutEffects {
                editableProject ?: throw IllegalStateException("editableProject must not be null")
                ProjectState.editableProject.modify(this) {
                    it.copy(name = action.name)
                }
            }
        }
}