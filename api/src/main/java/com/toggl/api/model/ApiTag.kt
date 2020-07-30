package com.toggl.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiTag(
    val id: Long,
    val workspace_id: Long,
    val name: String
)