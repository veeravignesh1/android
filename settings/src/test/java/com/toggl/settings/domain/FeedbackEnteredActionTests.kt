package com.toggl.settings.domain

import com.toggl.settings.common.CoroutineTest
import com.toggl.settings.common.createSettingsReducer
import com.toggl.settings.common.createSettingsState
import com.toggl.settings.common.testReduceNoEffects
import com.toggl.settings.common.testReduceState
import io.kotlintest.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The FeedbackEntered action")
class FeedbackEnteredActionTests : CoroutineTest() {
    private val reducer = createSettingsReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `should set the feedback message in the state`() = runBlockingTest {
        reducer.testReduceState(
            createSettingsState(feedbackMessage = "Test"),
            SettingsAction.FeedbackEntered("New Test")
        ) { state -> state.feedbackMessage shouldBe "New Test" }
    }

    @Test
    fun `shouldn't return any effects`() = runBlockingTest {
        reducer.testReduceNoEffects(createSettingsState(), SettingsAction.FeedbackEntered("Test"))
    }
}