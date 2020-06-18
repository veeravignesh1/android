package com.toggl.timer.project.domain

import arrow.optics.optics
import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.Project
import com.toggl.models.domain.Workspace
import com.toggl.models.validation.HSVColor
import com.toggl.timer.common.domain.TimerState

@optics
data class ProjectState(
    val editableProject: EditableProject,
    val projects: Map<Long, Project>,
    val workspaces: Map<Long, Workspace>,
    val timeEntryProjectId: Long?,
    val timeEntryDescription: String,
    val cursorPosition: Int,
    val customColor: HSVColor
) {
    companion object {

        fun fromTimerState(timerState: TimerState): ProjectState? {
            val editableProject = timerState.editableTimeEntry?.editableProject ?: return null

            return ProjectState(
                editableProject = editableProject,
                projects = timerState.projects,
                workspaces = timerState.workspaces,
                customColor = timerState.localState.customColor,
                timeEntryProjectId = timerState.editableTimeEntry.projectId,
                timeEntryDescription = timerState.editableTimeEntry.description,
                cursorPosition = timerState.localState.cursorPosition
            )
        }

        fun toTimerState(timerState: TimerState, projectState: ProjectState?) =
            projectState?.let {
                timerState.copy(
                    projects = projectState.projects,
                    editableTimeEntry = timerState.editableTimeEntry?.copy(
                        description = projectState.timeEntryDescription,
                        projectId = projectState.timeEntryProjectId,
                        editableProject = projectState.editableProject
                    ),
                    localState = timerState.localState.copy(customColor = projectState.customColor)
                )
            } ?: timerState
    }
}