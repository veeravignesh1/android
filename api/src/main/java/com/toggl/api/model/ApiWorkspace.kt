package com.toggl.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiWorkspace(
    val id: Long,
    val name: String
)