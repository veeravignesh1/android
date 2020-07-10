package com.toggl.models.domain

import com.toggl.models.validation.ApiToken
import com.toggl.models.validation.Email

data class User(
    val id: Long = 0,
    val apiToken: ApiToken,
    val email: Email.Valid,
    val name: String,
    val defaultWorkspaceId: Long
)
