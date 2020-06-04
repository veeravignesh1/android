package com.toggl.timer.project.domain

import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effectOf
import com.toggl.architecture.extensions.effects
import com.toggl.architecture.extensions.noEffect
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.isValid
import com.toggl.repository.extensions.toDto
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
            ProjectAction.CloseButtonTapped,
            ProjectAction.DialogDismissed -> effectOf(ProjectAction.Close)
            is ProjectAction.NameEntered -> state.mutateWithoutEffects {
                ProjectState.editableProject.modify(this) {
                    it.copy(name = action.name, error = EditableProject.ProjectError.None)
                }
            }
            ProjectAction.DoneButtonTapped -> {
                val (project, projectCanBeCreated) = state.mapState {
                    val listOfProjects = projects.values
                    editableProject to editableProject.isValid(listOfProjects)
                }

                if (projectCanBeCreated) createProject(project)
                else state.mutateWithoutEffects {
                    ProjectState.editableProject.modify(this) {
                        it.copy(error = EditableProject.ProjectError.ProjectAlreadyExists)
                    }
                }
            }
            is ProjectAction.ProjectCreated ->
                state.mutateWithoutEffects { copy(projects = projects + (action.project.id to action.project)) }
            ProjectAction.PrivateProjectSwitchTapped ->
                state.mutateWithoutEffects {
                    ProjectState.editableProject.modify(this) {
                        it.copy(isPrivate = !it.isPrivate)
                    }
                }
            is ProjectAction.ColorPicked -> state.mutateWithoutEffects {
                ProjectState.editableProject.modify(this) {
                    it.copy(color = action.color)
                }
            }
            is ProjectAction.WorkspacePicked -> state.mutateWithoutEffects {
                ProjectState.editableProject.modify(this) {
                    it.copy(workspaceId = action.workspace.id)
                }
            }
            is ProjectAction.ClientPicked -> state.mutateWithoutEffects {
                ProjectState.editableProject.modify(this) {
                    it.copy(clientId = action.client.id)
                }
            }
            ProjectAction.Close -> noEffect()
        }

    private fun createProject(editableProject: EditableProject) = effects(
        CreateProjectEffect(editableProject.toDto(), repository, dispatcherProvider)
    )
}
