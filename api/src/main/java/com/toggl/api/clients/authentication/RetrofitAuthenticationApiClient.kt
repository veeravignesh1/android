package com.toggl.api.clients.authentication

import com.toggl.api.extensions.basicAuthenticationWithPassword
import com.toggl.api.network.AuthenticationApi
import com.toggl.api.network.ResetPasswordBody
import com.toggl.models.domain.User
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitAuthenticationApiClient @Inject constructor(
    private val authenticationApi: AuthenticationApi
) : AuthenticationApiClient {
    override suspend fun login(email: Email.Valid, password: Password.Valid): User {
        val authHeader = email.basicAuthenticationWithPassword(password)
        return authenticationApi.login(authHeader)
    }

    override suspend fun resetPassword(email: Email.Valid): String {
        val body = ResetPasswordBody(email.toString())
        val result = authenticationApi.resetPassword(body)
        return result.trim('"')
    }
}