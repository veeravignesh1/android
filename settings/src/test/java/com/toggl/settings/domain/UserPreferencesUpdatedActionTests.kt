package com.toggl.settings.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.settings.common.CoroutineTest
import com.toggl.settings.common.createSettingsReducer
import com.toggl.settings.common.createSettingsState
import com.toggl.settings.common.createUserPreferences
import com.toggl.settings.common.testReduceNoEffects
import com.toggl.settings.common.testReduceState

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The UserPreferencesUpdated action")
class UserPreferencesUpdatedActionTests : CoroutineTest() {
    private val reducer = createSettingsReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `Should update user preferences`() = runBlockingTest {
        val initialState = createSettingsState(createUserPreferences(manualModeEnabled = false))
        val action = SettingsAction.UserPreferencesUpdated(
            createUserPreferences(
                manualModeEnabled = true
            )
        )
        reducer.testReduceState(
            initialState,
            action
        ) { state -> assertThat(state.userPreferences.manualModeEnabled).isTrue() }
    }

    @Test
    fun `shouldn't return any effects`() = runBlockingTest {
        reducer.testReduceNoEffects(
            createSettingsState(),
            SettingsAction.UserPreferencesUpdated(
                createUserPreferences(
                    manualModeEnabled = true
                )
            )
        )
    }
}