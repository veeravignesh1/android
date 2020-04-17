package com.toggl.timer.project.domain

import com.toggl.models.domain.Project
import com.toggl.timer.common.domain.EditableProject
import com.toggl.timer.common.domain.TimerState

data class ProjectState(
    val editableProject: EditableProject?,
    val projects: Map<Long, Project>
) {
    companion object {

        fun fromTimerState(timerState: TimerState) =
            ProjectState(
                editableProject = timerState.localState.editableTimeEntry?.editableProject,
                projects = timerState.projects
            )

        fun toTimerState(timerState: TimerState, projectState: ProjectState) =
            timerState.copy(
                projects = projectState.projects,
                localState = TimerState.LocalState.editableTimeEntry.modify(timerState.localState) {
                    it.copy(editableProject = projectState.editableProject)
                }
            )
    }
}