package com.toggl.settings.domain

import com.toggl.common.feature.navigation.Route
import com.toggl.models.domain.SettingsType
import com.toggl.settings.common.CoroutineTest
import com.toggl.settings.common.createSettingsReducer
import com.toggl.settings.common.createSettingsState
import com.toggl.settings.common.testReduceEffects
import com.toggl.settings.common.testReduceState
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
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
    fun `Should return some effect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialState,
            SettingsAction.SettingTapped(SettingsType.ManualMode)
        ) { effect -> effect.shouldNotBeEmpty() }
    }

    @Test
    fun `Tapping the About row in settings should navigate to the About view`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            SettingsAction.SettingTapped(SettingsType.About)
        ) { state ->
            state.backStack.size shouldBeGreaterThan initialState.backStack.size
            state.backStack.last() shouldBe Route.SettingsEdit(SettingsType.About)
        }
    }

    @Test
    fun `Tapping the Licenses row in settings should navigate to the Licenses view`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            SettingsAction.SettingTapped(SettingsType.Licenses)
        ) { state ->
            state.backStack.size shouldBeGreaterThan initialState.backStack.size
            state.backStack.last() shouldBe Route.SettingsEdit(SettingsType.Licenses)
        }
    }
}