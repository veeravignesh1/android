package com.toggl.calendar.datepicker.domain

import com.toggl.calendar.common.CoroutineTest
import com.toggl.calendar.common.testReduceNoEffects
import com.toggl.calendar.common.testReduceState
import com.toggl.models.common.SwipeDirection
import io.kotlintest.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The DaySwiped action")
class DaySwipedActionTests : CoroutineTest() {

    val reducer = createReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `When swiping left, updates the selected date to yesterday`() = runBlockingTest {
        val state = createInitialState()
        val initialDate = state.selectedDate

        reducer.testReduceState(state, CalendarDatePickerAction.DaySwiped(SwipeDirection.Left)) {
            it.selectedDate shouldBe initialDate.minusDays(1)
        }
    }

    @Test
    fun `When swiping right, updates the selected date to tomorrow`() = runBlockingTest {
        val state = createInitialState()
        val initialDate = state.selectedDate

        reducer.testReduceState(state, CalendarDatePickerAction.DaySwiped(SwipeDirection.Right)) {
            it.selectedDate shouldBe initialDate.plusDays(1)
        }
    }

    @Test
    fun `When the swiped date is not in available dates, does nothing`() = runBlockingTest {
        val state = createInitialState(availableDates = listOf())
        val initialDate = state.selectedDate

        reducer.testReduceState(state, CalendarDatePickerAction.DaySwiped(SwipeDirection.Left)) {
            it.selectedDate shouldBe initialDate
        }
    }

    @Test
    fun `Produces no effects`() = runBlockingTest {
        val state = createInitialState()

        reducer.testReduceNoEffects(state, CalendarDatePickerAction.DaySwiped(SwipeDirection.Left))
    }
}