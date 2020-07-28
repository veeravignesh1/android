package com.toggl.settings.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.models.domain.DateFormat
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
@DisplayName("The DateFormatSelected action")
class DateFormatSelectedActionTests : CoroutineTest() {
    private val initialState = createSettingsState()
    private val reducer = createSettingsReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `Should return correct effect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialState,
            SettingsAction.DateFormatSelected(dateFormat = DateFormat.DDMMYYYY_dash)
        ) { effects ->
            assertThat(effects).hasSize(1)
            assertThat(effects.first()).isInstanceOf(UpdateUserPreferencesEffect::class.java)
        }
    }

    @Test
    fun `Shouldn't change the state`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            SettingsAction.DateFormatSelected(dateFormat = DateFormat.DDMMYYYY_dash)
        ) { state -> assertThat(state).isEqualTo(initialState) }
    }
}