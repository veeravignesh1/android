package com.toggl.api.network

import com.toggl.api.network.models.feedback.FeedbackBody
import retrofit2.http.Body
import retrofit2.http.POST

internal interface FeedbackApi {
    @POST("mobile/feedback")
    suspend fun sendFeedback(@Body feedbackBody: FeedbackBody)
}
