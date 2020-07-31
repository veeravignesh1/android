package com.toggl.onboarding.login.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.api.exceptions.ForbiddenException
import com.toggl.api.exceptions.OfflineException
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.Failure
import com.toggl.architecture.core.Effect
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.repository.interfaces.UserRepository
import kotlinx.coroutines.withContext

class LogUserInEffect(
    private val apiClient: AuthenticationApiClient,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMessages: ErrorMessages,
    private val email: Email.Valid,
    private val password: Password.Valid
) : Effect<LoginAction> {
    override suspend fun execute(): LoginAction? = withContext(dispatcherProvider.io) {
        try {
            val user = apiClient.login(email, password)
            userRepository.set(user)
            LoginAction.SetUser(user)
        } catch (exception: Exception) {

            val errorMessage = when {
                exception is OfflineException -> errorMessages.offline
                exception !is ForbiddenException -> errorMessages.genericLoginError
                exception.remainingLoginAttempts == 0 -> errorMessages.accountIsLocked
                exception.remainingLoginAttempts == 1 -> errorMessages.oneMoreTryBeforeAccountLocked
                else -> errorMessages.incorrectEmailOrPassword
            }

            LoginAction.SetUserError(Failure(exception, errorMessage))
        }
    }

    data class ErrorMessages(
        val incorrectEmailOrPassword: String,
        val oneMoreTryBeforeAccountLocked: String,
        val accountIsLocked: String,
        val genericLoginError: String,
        val offline: String
    )
}
