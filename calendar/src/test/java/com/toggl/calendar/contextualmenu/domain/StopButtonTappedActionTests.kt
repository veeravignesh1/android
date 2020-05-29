package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.createCalendarEvent
import com.toggl.calendar.common.createTimeEntry
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.shouldEmitTimeEntryAction
import com.toggl.calendar.common.testReduceEffects
import com.toggl.calendar.common.testReduceException
import com.toggl.calendar.common.testReduceState
import com.toggl.calendar.exception.SelectedItemShouldBeAATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldNotBeNullException
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeNewException
import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeStoppedException
import com.toggl.models.domain.EditableTimeEntry
import io.kotlintest.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
@DisplayName("The StopButtonTapped action")
internal class StopButtonTappedActionTests {

    private val reducer = ContextualMenuReducer()

    @Test
    fun `throws if executed on a calendar item`() = runBlockingTest {
        val calendar = createCalendarEvent()
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedCalendarEvent(calendar))

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.StopButtonTapped,
            exception = SelectedItemShouldBeAATimeEntryException::class.java
        )
    }

    @Test
    fun `throws if executed on a null selected item`() = runBlockingTest {
        val initialState = createInitialState(selectedItem = null)

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.StopButtonTapped,
            exception = SelectedItemShouldNotBeNullException::class.java
        )
    }

    @Test
    fun `throws if executed on a new time entry`() = runBlockingTest {
        val timeEntry = EditableTimeEntry.empty(1)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(timeEntry))

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.StopButtonTapped,
            exception = TimeEntryShouldNotBeNewException::class.java
        )
    }

    @Test
    fun `throws if executed on a stopped time entry`() = runBlockingTest {
        val timeEntry = createTimeEntry(1, duration = Duration.ofMinutes(3), startTime = OffsetDateTime.now())
        val editableTimeEntry = EditableTimeEntry.fromSingle(timeEntry)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry))

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.StopButtonTapped,
            exception = TimeEntryShouldNotBeStoppedException::class.java
        )
    }

    @Test
    fun `sets the selectedItem to null`() = runBlockingTest {
        val timeEntry = createTimeEntry(1, startTime = OffsetDateTime.now().minusMinutes(3), duration = null)
        val editableTimeEntry = EditableTimeEntry.fromSingle(timeEntry)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry))

        reducer.testReduceState(initialState, ContextualMenuAction.StopButtonTapped) { state ->
            state shouldBe initialState.copy(selectedItem = null)
        }
    }

    @Test
    fun `returns an effect to stop the time entry`() = runBlockingTest {
        val timeEntry = createTimeEntry(1, startTime = OffsetDateTime.now().minusMinutes(3), duration = null)
        val editableTimeEntry = EditableTimeEntry.fromSingle(timeEntry)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry))

        reducer.testReduceEffects(
            initialState = initialState,
            action = ContextualMenuAction.StopButtonTapped
        ) { effects -> effects.single().shouldEmitTimeEntryAction<ContextualMenuAction.TimeEntryHandling, TimeEntryAction.StopRunningTimeEntry>() }
    }
}