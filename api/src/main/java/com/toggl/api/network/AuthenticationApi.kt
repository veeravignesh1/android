package com.toggl.api.network

import com.squareup.moshi.JsonClass
import com.toggl.api.models.ApiUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

internal interface AuthenticationApi {
    @GET("me")
    suspend fun login(@Header("Authorization") authHeader: String): ApiUser

    @POST("me/lost_passwords")
    suspend fun resetPassword(@Body passwordForgottenData: ResetPasswordBody): String
}

@JsonClass(generateAdapter = true)
data class ResetPasswordBody(val email: String)
