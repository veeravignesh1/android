package com.toggl.settings.domain

import com.toggl.api.feedback.FeedbackApi
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.models.domain.PlatformInfo
import com.toggl.models.domain.User
import kotlinx.coroutines.withContext

class SendFeedbackEffect(
    private val feedbackMessage: String,
    private val user: User,
    private val platformInfo: PlatformInfo,
    private val feedbackDataBuilder: FeedbackDataBuilder,
    private val feedbackApi: FeedbackApi,
    private val dispatcherProvider: DispatcherProvider
) : Effect<SettingsAction> {
    override suspend fun execute(): SettingsAction =
        withContext(dispatcherProvider.io) {
            try {
                val feedbackData = feedbackDataBuilder.assembleFeedbackData()
                feedbackApi.sendFeedback(user, feedbackMessage, platformInfo, feedbackData)
                SettingsAction.FeedbackSent
            } catch (throwable: Throwable) {
                SettingsAction.SetSendFeedbackError(throwable)
            }
        }
}