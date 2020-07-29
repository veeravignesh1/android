package com.toggl.onboarding.passwordreset.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.api.exceptions.ApiException
import com.toggl.api.exceptions.BadRequestException
import com.toggl.api.exceptions.OfflineException
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.Failure
import com.toggl.architecture.core.Effect
import com.toggl.models.validation.Email
import kotlinx.coroutines.withContext
import java.lang.Exception

class SendPasswordResetEmailEffect(
    private val authenticationApiClient: AuthenticationApiClient,
    private val dispatcherProvider: DispatcherProvider,
    private val offlineMessage: String,
    private val genericErrorMessage: String,
    private val emailDoesNotExistMessage: String,
    private val email: Email.Valid
) : Effect<PasswordResetAction> {
    override suspend fun execute(): PasswordResetAction? = withContext(dispatcherProvider.io) {
        try {
            val message = authenticationApiClient.resetPassword(email)
            PasswordResetAction.PasswordResetEmailSent(message)
        } catch (ex: Exception) {

            val failure = Failure(ex, when (ex) {
                is BadRequestException -> emailDoesNotExistMessage
                is OfflineException -> offlineMessage
                is ApiException -> ex.errorMessage
                else -> genericErrorMessage
            })

            PasswordResetAction.PasswordResetEmailFailed(failure)
        }
    }
}