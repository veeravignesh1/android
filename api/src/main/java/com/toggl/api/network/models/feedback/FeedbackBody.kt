package com.toggl.api.network.models.feedback

import java.io.Serializable

internal data class FeedbackBody(
    val email: String,
    val message: String,
    val data: Map<String, String>
) : Serializable