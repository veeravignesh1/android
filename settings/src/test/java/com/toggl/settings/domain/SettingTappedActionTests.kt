package com.toggl.settings.domain

import com.toggl.common.feature.navigation.Route
import com.toggl.models.domain.SelectedSetting
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
@DisplayName("The SettingTapped action")
class SettingTappedActionTests : CoroutineTest() {
    private val initialState = createSettingsState()
    private val reducer = createSettingsReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `Should modify back stack correctly`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            SettingsAction.SettingTapped(SelectedSetting.About)
        ) { state -> state.backStack.last() shouldBe Route.SettingsEdit(SelectedSetting.About) }
    }

    @Test
    fun `shouldn't return any effects`() = runBlockingTest {
        reducer.testReduceNoEffects(initialState, SettingsAction.SettingTapped(SelectedSetting.About))
    }
}