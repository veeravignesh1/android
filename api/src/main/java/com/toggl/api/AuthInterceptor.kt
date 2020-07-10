package com.toggl.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authString: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = authString?.let {
            chain.request()
                .newBuilder()
                .addHeader("Authorization", it)
                .build()
        } ?: chain.request()

        return chain.proceed(request)
    }
}