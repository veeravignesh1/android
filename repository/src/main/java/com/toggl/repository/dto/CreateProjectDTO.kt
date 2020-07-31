package com.toggl.repository.dto

data class CreateProjectDTO(
    val name: String,
    val color: String,
    val active: Boolean,
    val isPrivate: Boolean,
    val billable: Boolean?,
    val workspaceId: Long,
    val clientId: Long?
)