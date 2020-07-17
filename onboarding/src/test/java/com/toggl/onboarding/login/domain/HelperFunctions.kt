package com.toggl.onboarding.login.domain

import com.toggl.architecture.Loadable
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password

fun emptyState() = LoginState(
    Loadable.Uninitialized,
    emptyList(),
    Email.Invalid(""),
    Password.Invalid("")
)