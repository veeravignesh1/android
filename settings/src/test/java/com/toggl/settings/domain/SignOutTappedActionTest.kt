package com.toggl.settings.domain

import com.google.common.truth.Truth.assertThat
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
@DisplayName("The SignOutTapped action")
class SignOutTappedActionTest : CoroutineTest() {
    private val reducer = createSettingsReducer(dispatcherProvider = dispatcherProvider)
    private val initialSate = createSettingsState()

    @Test
    fun `shouldn't change the state`() = runBlockingTest {
        reducer.testReduceState(
            initialSate,
            SettingsAction.SignOutTapped
        ) { state -> assertThat(state).isEqualTo(initialSate) }
    }

    @Test
    fun `should return sign out effect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialSate,
            SettingsAction.SignOutTapped
        ) { effects ->
            assertThat(effects).hasSize(1)
            assertThat(effects.first()).isInstanceOf(SignOutEffect::class.java)
        }
    }
}