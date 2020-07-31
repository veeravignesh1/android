package com.toggl.onboarding.login.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.repository.interfaces.UserRepository
import kotlinx.coroutines.withContext

class LogUserInEffect(
    private val apiClient: AuthenticationApiClient,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val email: Email.Valid,
    private val password: Password.Valid
) : Effect<LoginAction> {
    override suspend fun execute(): LoginAction? = withContext(dispatcherProvider.io) {
        try {
            val user = apiClient.login(email, password)
            userRepository.set(user)
            LoginAction.SetUser(user)
        } catch (throwable: Throwable) {
            LoginAction.SetUserError(throwable)
        }
    }
}