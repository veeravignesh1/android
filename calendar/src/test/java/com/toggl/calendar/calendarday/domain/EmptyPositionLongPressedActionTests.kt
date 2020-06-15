package com.toggl.calendar.calendarday.domain

import com.toggl.calendar.common.CoroutineTest
import com.toggl.calendar.common.createCalendarDayReducer
import com.toggl.calendar.common.createInitialState
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.testReduceNoEffects
import com.toggl.calendar.common.testReduceState
import com.toggl.common.Constants
import com.toggl.models.domain.EditableTimeEntry
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
@DisplayName("The EmptyPositionLongPressed action")
class EmptyPositionLongPressedActionTests : CoroutineTest() {
    private val state = createInitialState()
    private val reducer = createCalendarDayReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `should set selectedItem with the start time`() = runBlockingTest {
        val expectedStartTime: OffsetDateTime = mockk()
        reducer.testReduceState(
            state,
            CalendarDayAction.EmptyPositionLongPressed(expectedStartTime)
        ) { state ->
            state shouldBe state.copy(
                selectedItem = SelectedCalendarItem.SelectedTimeEntry(
                    EditableTimeEntry.empty(1).copy(
                        startTime = expectedStartTime,
                        duration = Constants.TimeEntry.defaultTimeEntryDuration
                    )
                )
            )
        }
    }

    @Test
    fun `shouldn't return any effect`() = runBlockingTest {
        reducer.testReduceNoEffects(state, CalendarDayAction.EmptyPositionLongPressed(mockk()))
    }
}