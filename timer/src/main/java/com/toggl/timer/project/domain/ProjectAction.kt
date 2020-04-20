package com.toggl.timer.project.domain

import com.toggl.models.domain.Project
import com.toggl.timer.common.domain.TimerAction

sealed class ProjectAction {
    data class NameEntered(val name: String) : ProjectAction()
    object DoneButtonTapped : ProjectAction()
    data class ProjectCreated(val project: Project) : ProjectAction()

    companion object {
        fun fromTimerAction(timerAction: TimerAction): ProjectAction? =
            if (timerAction !is TimerAction.Project) null
            else timerAction.projectAction

        fun toTimerAction(projectAction: ProjectAction): TimerAction =
            TimerAction.Project(projectAction)
    }
}

fun ProjectAction.formatForDebug() =
    when (this) {
        is ProjectAction.NameEntered -> "Project name entered: $name"
        is ProjectAction.DoneButtonTapped -> "Project creation confirmed"
        is ProjectAction.ProjectCreated -> "Project created with id ${project.id}"
    }