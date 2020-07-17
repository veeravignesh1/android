package com.toggl.api.login

import com.toggl.api.extensions.basicAuthenticationWithPassword
import com.toggl.api.network.LoginApi
import com.toggl.models.domain.User
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password

internal class RetrofitLoginApiClient(
    private val loginApi: LoginApi
) : LoginApiClient {
    override suspend fun login(email: Email.Valid, password: Password.Valid): User {
        val authHeader = email.basicAuthenticationWithPassword(password)
        return loginApi.login(authHeader)
    }
}