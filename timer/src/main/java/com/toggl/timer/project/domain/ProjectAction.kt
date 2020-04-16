package com.toggl.timer.project.domain

import com.toggl.timer.common.domain.TimerAction

sealed class ProjectAction {
    data class NameEntered(val name: String) : ProjectAction()

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
    }