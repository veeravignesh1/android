package com.toggl.api.network

import com.toggl.models.domain.User
import retrofit2.http.GET
import retrofit2.http.Header

internal interface LoginApi {
    @GET("me")
    suspend fun login(@Header("Authorization") authHeader: String): User
}
