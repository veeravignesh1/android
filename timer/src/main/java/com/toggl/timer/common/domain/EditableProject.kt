package com.toggl.timer.common.domain

import arrow.optics.optics
import com.toggl.models.domain.Project
import com.toggl.repository.dto.CreateProjectDTO

@optics
data class EditableProject(
    val name: String = "",
    val color: String = "",
    val active: Boolean = true,
    val isPrivate: Boolean = true,
    val billable: Boolean? = null,
    val workspaceId: Long,
    val clientId: Long? = null,
    val error: ProjectError = ProjectError.None
) {
    enum class ProjectError {
        None,
        ProjectAlreadyExists
    }

    companion object {
        fun empty(workspaceId: Long) = EditableProject(workspaceId = workspaceId)
    }
}

fun EditableProject.toDto() = CreateProjectDTO(
    name,
    color,
    active,
    isPrivate,
    billable,
    workspaceId,
    clientId
)

fun EditableProject.isValid(projects: Collection<Project>): Boolean =
    projects.none {
        it.name == name &&
        it.workspaceId == workspaceId &&
        it.clientId == clientId
    }