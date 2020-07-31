package com.toggl.models.domain

data class Client(
    val id: Long = 0L,
    val name: String,
    val workspaceId: Long
)
