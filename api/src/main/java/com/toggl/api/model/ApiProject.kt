package com.toggl.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiProject(
    val id: Long,
    val workspace_id: Long,
    val client_id: Long?,
    val name: String,
    val isPrivate: Boolean,
    val active: Boolean
)