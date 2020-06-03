package com.toggl.calendar.calendarday.domain

import com.toggl.calendar.common.CoroutineTest
import com.toggl.calendar.common.createInitialState
import com.toggl.calendar.common.createTimeEntry
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.testReduceException
import com.toggl.calendar.common.testReduceNoEffects
import com.toggl.calendar.common.testReduceState
import com.toggl.calendar.exception.SelectedItemShouldBeATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldNotBeNullException
import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeNewException
import com.toggl.models.domain.EditableTimeEntry
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
@DisplayName("The StartTimeDragged action")
class StartTimeDraggedActionTests : CoroutineTest() {

    private val reducer = CalendarDayReducer(mockk(), dispatcherProvider)

    private val startTime = OffsetDateTime.of(2005, 5, 5, 5, 5, 0, 0, ZoneOffset.UTC)
    private val duration = Duration.ofHours(5)
    private val endTime = startTime.plus(duration)

    private val validEditableTimeEntry = EditableTimeEntry.fromSingle(createTimeEntry(
        1,
        "test",
        startTime,
        duration
    ))

    @Test
    fun `throws if executed on a calendar item`() = runBlockingTest {
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedCalendarEvent(mockk()))
        reducer.testReduceException(
            initialState,
            CalendarDayAction.StartTimeDragged(OffsetDateTime.MAX),
            SelectedItemShouldBeATimeEntryException::class.java
        )
    }

    @Test
    fun `throws if executed on a null selected item`() = runBlockingTest {
        val initialState = createInitialState(selectedItem = null)
        reducer.testReduceException(
            initialState,
            CalendarDayAction.StartTimeDragged(OffsetDateTime.MAX),
            SelectedItemShouldNotBeNullException::class.java
        )
    }

    @Test
    fun `throws if executed on a new time entry`() = runBlockingTest {
        val timeEntry = EditableTimeEntry.empty(1)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(timeEntry))
        reducer.testReduceException(
            initialState,
            CalendarDayAction.StartTimeDragged(OffsetDateTime.MAX),
            TimeEntryShouldNotBeNewException::class.java
        )
    }

    @Test
    fun `shouldn't change the state if new start time is after original end time`() = runBlockingTest {
        val selectedItem = SelectedCalendarItem.SelectedTimeEntry(validEditableTimeEntry)
        val initialState = createInitialState(selectedItem = selectedItem)
        val newStart = endTime.plusHours(1)
        reducer.testReduceState(
            initialState,
            CalendarDayAction.StartTimeDragged(newStart)
        ) { state ->
            state shouldBe initialState
        }
    }

    @Test
    fun `shouldn't return any effects`() = runBlockingTest {
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(validEditableTimeEntry))
        val newStart = startTime.minusHours(1)
        reducer.testReduceNoEffects(initialState, CalendarDayAction.StartTimeDragged(newStart))
    }

    @Test
    fun `should set the new start time and increase duration when moved back`() = runBlockingTest {
        val selectedItem = SelectedCalendarItem.SelectedTimeEntry(validEditableTimeEntry)
        val initialState = createInitialState(selectedItem = selectedItem)
        val newStart = startTime.minusHours(1)
        val expectedNewDuration = duration.plusHours(1)
        reducer.testReduceState(
            initialState,
            CalendarDayAction.StartTimeDragged(newStart)
        ) { state ->
            state shouldBe initialState.copy(
                selectedItem = SelectedCalendarItem.SelectedTimeEntry(
                    validEditableTimeEntry.copy(
                        startTime = newStart,
                        duration = expectedNewDuration
                    )
                )
            )
        }
    }

    @Test
    fun `should set the new start time and decrease duration when moved forward`() = runBlockingTest {
        val selectedItem = SelectedCalendarItem.SelectedTimeEntry(validEditableTimeEntry)
        val initialState = createInitialState(selectedItem = selectedItem)
        val newStart = startTime.plusHours(1)
        val expectedNewDuration = duration.minusHours(1)
        reducer.testReduceState(initialState,
            CalendarDayAction.StartTimeDragged(newStart)
        ) { state ->
            state shouldBe initialState.copy(
                selectedItem = SelectedCalendarItem.SelectedTimeEntry(
                    validEditableTimeEntry.copy(
                        startTime = newStart,
                        duration = expectedNewDuration
                    )
                )
            )
        }
    }
}
