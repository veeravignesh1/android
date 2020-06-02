package com.toggl.timer.project.domain

import com.toggl.architecture.extensions.noEffect
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.Workspace
import com.toggl.timer.common.domain.TimerAction

sealed class ProjectAction {
    data class NameEntered(val name: String) : ProjectAction()
    object DoneButtonTapped : ProjectAction()
    data class ProjectCreated(val project: Project) : ProjectAction()
    object PrivateProjectSwitchTapped : ProjectAction()
    object CloseButtonTapped : ProjectAction()
    object DialogDismissed : ProjectAction()
    data class ColorPicked(val color: String) : ProjectAction()
    data class WorkspacePicked(val workspace: Workspace) : ProjectAction()
    data class ClientPicked(val client: Client) : ProjectAction()

    companion object {
        fun fromTimerAction(timerAction: TimerAction): ProjectAction? =
            if (timerAction !is TimerAction.Project) null
            else timerAction.projectAction

        fun toTimerAction(projectAction: ProjectAction): TimerAction =
            TimerAction.Project(projectAction)
    }
}

fun ProjectAction.isCloseAction() = when (this) {
    ProjectAction.CloseButtonTapped,
    ProjectAction.DialogDismissed,
    is ProjectAction.ProjectCreated -> true
    else -> false
}

fun ProjectAction.formatForDebug() =
    when (this) {
        is ProjectAction.NameEntered -> "Project name entered: $name"
        is ProjectAction.DoneButtonTapped -> "Project creation confirmed"
        is ProjectAction.ProjectCreated -> "Project created with id ${project.id}"
        is ProjectAction.PrivateProjectSwitchTapped -> "Private project switch tapped"
        ProjectAction.CloseButtonTapped -> "Close button tapped"
        ProjectAction.DialogDismissed -> "Dialog dismissed by swipping"
        is ProjectAction.ColorPicked -> "Selected color $color"
        is ProjectAction.WorkspacePicked -> "Selected workspace with id ${workspace.id}"
        is ProjectAction.ClientPicked -> "Selected client wit id ${client.id}"
    }