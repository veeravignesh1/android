package com.toggl.onboarding.login.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.Loadable
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.backStackOf
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.repository.interfaces.UserRepository
import io.mockk.mockk

fun emptyLoginState() = LoginState(
    Loadable.Uninitialized,
    backStackOf(Route.Welcome, Route.Login),
    Email.Invalid(""),
    Password.Invalid("")
)

fun createLoginReducer(
    loginApi: AuthenticationApiClient = mockk(),
    userRepository: UserRepository = mockk(),
    dispatcherProvider: DispatcherProvider = mockk()
) = LoginReducer(
    apiClient = loginApi,
    userRepository = userRepository,
    dispatcherProvider = dispatcherProvider
)