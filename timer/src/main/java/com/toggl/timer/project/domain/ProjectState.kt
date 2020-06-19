package com.toggl.timer.project.domain

import arrow.optics.optics
import com.toggl.common.feature.navigation.getEditableProjectIfAny
import com.toggl.common.feature.navigation.getEditableTimeEntryIfAny
import com.toggl.common.feature.navigation.updateEditableProject
import com.toggl.common.feature.navigation.updateEditableTimeEntry
import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Project
import com.toggl.models.domain.Workspace
import com.toggl.models.validation.HSVColor
import com.toggl.timer.common.domain.TimerState

@optics
data class ProjectState(
    val editableProject: EditableProject,
    val editableTimeEntry: EditableTimeEntry,
    val projects: Map<Long, Project>,
    val workspaces: Map<Long, Workspace>,
    val cursorPosition: Int,
    val customColor: HSVColor
) {
    companion object {

        fun fromTimerState(timerState: TimerState): ProjectState? {
            val editableTimeEntry = timerState.backStack.getEditableTimeEntryIfAny() ?: return null
            val editableProject = timerState.backStack.getEditableProjectIfAny() ?: return null

            return ProjectState(
                editableProject = editableProject,
                editableTimeEntry = editableTimeEntry,
                projects = timerState.projects,
                workspaces = timerState.workspaces,
                customColor = timerState.localState.customColor,
                cursorPosition = timerState.localState.cursorPosition
            )
        }

        fun toTimerState(timerState: TimerState, projectState: ProjectState?) =
            projectState?.let {
                timerState.copy(
                    projects = projectState.projects,
                    localState = timerState.localState.copy(customColor = projectState.customColor),
                    backStack = timerState.backStack
                        .updateEditableTimeEntry(projectState.editableTimeEntry)
                        .updateEditableProject(projectState.editableProject)
                )
            } ?: timerState
    }
}