package com.toggl.api.network.interceptors

import com.toggl.api.ApiTokenProvider
import com.toggl.api.extensions.basicAuthenticationHeader
import com.toggl.models.validation.ApiToken
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenProvider: ApiTokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = when (val token = tokenProvider.getApiToken()) {
            ApiToken.Invalid -> chain.request()
            is ApiToken.Valid -> chain.request()
                .newBuilder()
                .addHeader("Authorization", token.basicAuthenticationHeader())
                .build()
        }

        return chain.proceed(request)
    }
}