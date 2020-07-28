package com.toggl.calendar.calendarday.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.calendar.common.CoroutineTest
import com.toggl.calendar.common.createCalendarDayReducer
import com.toggl.calendar.common.testReduceEffects
import com.toggl.calendar.common.testReduceState

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
@ExperimentalCoroutinesApi
@DisplayName("The CalendarViewAppeared action")
internal class CalendarViewAppearedActionTests : CoroutineTest() {

    private val initialState = createInitialState()
    private val reducer = createCalendarDayReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `Shouldn't change the state`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            CalendarDayAction.CalendarViewAppeared
        ) { state ->
            assertThat(state).isEqualTo(initialState)
        }
    }

    @Test
    fun `Should return correct effect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialState,
            CalendarDayAction.CalendarViewAppeared
        ) { effects ->
            assertThat(effects).hasSize(1)
            assertThat(effects.first()).isInstanceOf(FetchCalendarEventsEffect::class.java)
        }
    }
}