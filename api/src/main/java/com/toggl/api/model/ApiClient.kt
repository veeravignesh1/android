package com.toggl.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiClient(
    val id: Long,
    val wid: Long,
    val name: String
)