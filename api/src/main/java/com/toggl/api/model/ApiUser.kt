package com.toggl.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiUser(
    val id: Long,
    val api_token: String
)