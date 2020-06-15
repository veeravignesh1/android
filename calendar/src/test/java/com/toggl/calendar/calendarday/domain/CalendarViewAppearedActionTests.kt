package com.toggl.calendar.calendarday.domain

import com.toggl.calendar.common.CoroutineTest
import com.toggl.calendar.common.createCalendarDayReducer
import com.toggl.calendar.common.testReduceEffects
import com.toggl.calendar.common.testReduceState
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The CalendarViewAppeared action")
internal class CalendarViewAppearedActionTests : CoroutineTest() {

    private val initialState = createCalendarDayState()
    private val reducer = createCalendarDayReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `Shouldn't change the state`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            CalendarDayAction.CalendarViewAppeared
        ) { state ->
            state shouldBe initialState
        }
    }

    @Test
    fun `Should return correct effect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialState,
            CalendarDayAction.CalendarViewAppeared
        ) { effects ->
            effects shouldHaveSize 1
            effects.first().shouldBeInstanceOf<FetchCalendarEventsEffect>()
        }
    }
}