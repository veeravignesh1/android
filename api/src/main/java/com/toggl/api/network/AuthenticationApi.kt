package com.toggl.api.network

import com.toggl.models.domain.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

internal interface AuthenticationApi {
    @GET("me")
    suspend fun login(@Header("Authorization") authHeader: String): User

    @POST("me/lost_passwords")
    suspend fun resetPassword(@Body passwordForgottenData: ResetPasswordBody): String
}

data class ResetPasswordBody(val email: String)