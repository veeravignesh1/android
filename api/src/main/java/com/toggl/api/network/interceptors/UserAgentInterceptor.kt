package com.toggl.api.network.interceptors

import com.toggl.models.domain.PlatformInfo
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAgentInterceptor @Inject constructor(
    private val platformInfo: PlatformInfo
) : Interceptor {

    private val userAgent = "AndroidAurora/${platformInfo.version}"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }
}