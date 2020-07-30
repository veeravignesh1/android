package com.toggl.api.clients

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.api.clients.authentication.RetrofitAuthenticationApiClient
import com.toggl.api.clients.feedback.FeedbackApiClient
import com.toggl.api.clients.feedback.RetrofitFeedbackApiClient
import com.toggl.api.exceptions.ApiException
import com.toggl.api.exceptions.OfflineException
import com.toggl.models.domain.FeedbackData
import com.toggl.models.domain.PlatformInfo
import com.toggl.models.domain.User
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ErrorHandlingProxyClient @Inject constructor(
    private val authenticationApiClient: RetrofitAuthenticationApiClient,
    private val feedbackApiClient: RetrofitFeedbackApiClient
) : AuthenticationApiClient, FeedbackApiClient {
    override suspend fun login(email: Email.Valid, password: Password.Valid): User {
        try {
            return authenticationApiClient.login(email, password)
        } catch (exception: Exception) {
            throw handledException(exception)
        }
    }

    override suspend fun resetPassword(email: Email.Valid): String {
        try {
            return authenticationApiClient.resetPassword(email)
        } catch (exception: Exception) {
            throw handledException(exception)
        }
    }

    override suspend fun sendFeedback(user: User, message: String, platformInfo: PlatformInfo, feedbackData: FeedbackData) {
        try {
            return feedbackApiClient.sendFeedback(user, message, platformInfo, feedbackData)
        } catch (exception: Exception) {
            throw handledException(exception)
        }
    }

    private fun handledException(exception: Exception) =
        when (exception) {
            is UnknownHostException -> OfflineException()
            is HttpException -> ApiException.from(exception.code(), null)
            else -> exception
        }
}
