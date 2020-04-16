package com.toggl.timer.common.domain

import arrow.optics.optics

@optics
data class EditableProject(
    val name: String = "",
    val color: String = "",
    val active: Boolean = true,
    val isPrivate: Boolean = true,
    val billable: Boolean? = null,
    val workspaceId: Long,
    val clientId: Long? = null
) {
    companion object {
        fun empty(workspaceId: Long) = EditableProject(workspaceId = workspaceId)
    }
}
