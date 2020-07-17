package com.toggl.api.network

import retrofit2.http.Body
import retrofit2.http.POST
import java.io.Serializable

internal interface FeedbackApi {
    @POST("mobile/feedback")
    suspend fun sendFeedback(@Body feedbackBody: FeedbackBody)
}

data class FeedbackBody(
    val email: String,
    val message: String,
    val data: Map<String, String>
) : Serializable