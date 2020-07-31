package com.toggl.onboarding.signup.domain

import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.backStackOf
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password

fun emptySignUpState() = SignUpState(
    Email.Invalid(""),
    Password.Invalid(""),
    backStackOf(Route.Welcome, Route.Login)
)

fun createSignUpReducer() = SignUpReducer()
