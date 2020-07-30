package com.toggl.models.domain

data class Task(
    val id: Long = 0,
    val name: String,
    val active: Boolean,
    val projectId: Long,
    val workspaceId: Long,
    val userId: Long?
)
