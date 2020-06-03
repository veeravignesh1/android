package com.toggl.timer.project.domain

import arrow.optics.optics
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.Workspace

@optics
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
    object Close : ProjectAction()

    companion object
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
        ProjectAction.Close -> "Project view closed"
    }