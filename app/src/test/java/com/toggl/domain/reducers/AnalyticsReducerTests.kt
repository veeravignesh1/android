package com.toggl.domain.reducers

import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.extensions.noEffect
import com.toggl.common.CoroutineTest
import com.toggl.common.feature.services.analytics.AnalyticsService
import com.toggl.common.feature.services.analytics.Event
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.domain.loading.LoadingAction
import com.toggl.timer.common.domain.TimerAction
import com.toggl.timer.log.domain.TimeEntriesLogAction
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class AnalyticsReducerTests : CoroutineTest() {
    val analyticsService = mockk<AnalyticsService>(relaxed = true)
    val analyticsReducer = AnalyticsReducer(analyticsService)
    val initialState = AppState()

    @Test
    fun `The AnalyticsReducer's reduce function does not change the state`() {
        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        val appAction = mockk<AppAction>()

        analyticsReducer.reduce(mutableValue, appAction)

        initialState shouldBe state
    }

    @Test
    fun `The AnalyticsReducer's reduce function does not produce any effect`() {
        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        val appAction = mockk<AppAction>()

        val effect = analyticsReducer.reduce(mutableValue, appAction)

        effect shouldBe noEffect()
    }

    @Test
    fun `The AnalyticsReducer's reduce function does not call the AnalyticsService's track method if the event is null`() {
        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        val appAction = AppAction.Loading(LoadingAction.StartLoading)

        analyticsReducer.reduce(mutableValue, appAction)

        verify { analyticsService wasNot Called }
    }

    @Test
    fun `The AnalyticsReducer's reduce function calls the AnalyticsService's track method if the event is not null`() {
        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        val appAction = AppAction.Timer(TimerAction.TimeEntriesLog(TimeEntriesLogAction.UndoButtonTapped))
        val expectedEvent = Event.undoTapped()

        analyticsReducer.reduce(mutableValue, appAction)

        verify(exactly = 1) { analyticsService.track(expectedEvent) }
    }
}

fun AppState.toMutableValue(setFunction: (AppState) -> Unit) =
    MutableValue({ this }, setFunction)
