package com.toggl.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiTask(
    val id: Long,
    val name: String,
    val workspace_id: Long,
    val project_id: Long,
    val user_id: Long?,
    val active: Boolean
)