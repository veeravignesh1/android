package com.toggl.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiTimeEntry(
    val id: Long,
    val workspace_id: Long,
    val project_id: Long?,

    val duration: Int,
    val description: String
)