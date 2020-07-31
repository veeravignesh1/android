package com.toggl.api.clients

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.api.clients.authentication.RetrofitAuthenticationApiClient
import com.toggl.api.clients.feedback.FeedbackApiClient
import com.toggl.api.clients.feedback.RetrofitFeedbackApiClient
import com.toggl.api.clients.reports.ReportsApiClient
import com.toggl.api.clients.reports.RetrofitReportsApiClient
import com.toggl.api.exceptions.ApiException
import com.toggl.api.exceptions.ForbiddenException.Companion.remainingLoginAttemptsHeaderName
import com.toggl.api.exceptions.OfflineException
import com.toggl.api.models.ReportsTotals
import com.toggl.models.domain.FeedbackData
import com.toggl.models.domain.PlatformInfo
import com.toggl.models.domain.User
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ErrorHandlingProxyClient @Inject constructor(
    private val authenticationApiClient: RetrofitAuthenticationApiClient,
    private val feedbackApiClient: RetrofitFeedbackApiClient,
    private val reportsApiClient: RetrofitReportsApiClient
) : AuthenticationApiClient, FeedbackApiClient, ReportsApiClient {
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

    override suspend fun getTotals(
        userId: Long,
        workspaceId: Long,
        startDate: OffsetDateTime,
        endDate: OffsetDateTime
    ): ReportsTotals {
        try {
            return reportsApiClient.getTotals(userId, workspaceId, startDate, endDate)
        } catch (exception: Exception) {
            throw handledException(exception)
        }
    }

    private fun handledException(exception: Exception) =
        when (exception) {
            is UnknownHostException -> OfflineException()
            is HttpException -> ApiException.from(
                exception.code(),
                null,
                exception.tryParsingNumberOfAttemptsBeforeAccountBlock()
            )
            else -> exception
        }

    private fun HttpException.tryParsingNumberOfAttemptsBeforeAccountBlock(): Int? {
        val headers = response()?.headers() ?: return null
        if (headers.size() == 0) return null
        val remainingAttemptsHeader = headers.get(remainingLoginAttemptsHeaderName) ?: return null
        return remainingAttemptsHeader.toIntOrNull()
    }
}
