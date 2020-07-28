package com.toggl.settings.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.architecture.Loadable
import com.toggl.settings.common.CoroutineTest
import com.toggl.settings.common.createSettingsReducer
import com.toggl.settings.common.createSettingsState
import com.toggl.settings.common.testReduceEffects
import com.toggl.settings.common.testReduceState

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The SendFeedbackTapped action")
class SendFeedbackTappedActionTests : CoroutineTest() {
    private val initialState = createSettingsState()
    private val reducer = createSettingsReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `Should modify the state to indicate that the feedback is being sent`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            SettingsAction.SendFeedbackTapped("message")
        ) { state -> assertThat(state.localState.sendFeedbackRequest).isInstanceOf(Loadable.Loading::class.java) }
    }

    @Test
    fun `Should emit the SendFeedbackEffect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialState,
            SettingsAction.SendFeedbackTapped("message")
        ) { effect ->
            assertThat(effect).hasSize(1)
            assertThat(effect.first()).isInstanceOf(SendFeedbackEffect::class.java)
        }
    }
}