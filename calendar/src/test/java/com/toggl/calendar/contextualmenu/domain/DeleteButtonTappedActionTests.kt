package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.createCalendarEvent
import com.toggl.calendar.common.createTimeEntry
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.shouldEmitTimeEntryAction
import com.toggl.calendar.common.testReduceEffects
import com.toggl.calendar.common.testReduceException
import com.toggl.calendar.common.testReduceState
import com.toggl.calendar.exception.SelectedItemShouldBeATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldNotBeNullException
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeNewException
import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeRunningException
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
@DisplayName("The DeleteButtonTapped action")
internal class DeleteButtonTappedActionTests {

    private val timeService = mockk<TimeService> { every { now() } returns OffsetDateTime.now() }
    private val reducer = ContextualMenuReducer(timeService)

    @Test
    fun `throws if executed on a calendar item`() = runBlockingTest {
        val calendar = createCalendarEvent()
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedCalendarEvent(calendar))

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.DeleteButtonTapped,
            exception = SelectedItemShouldBeATimeEntryException::class.java
        )
    }

    @Test
    fun `throws if executed on a null selected item`() = runBlockingTest {
        val initialState = createInitialState(selectedItem = null)

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.DeleteButtonTapped,
            exception = SelectedItemShouldNotBeNullException::class.java
        )
    }

    @Test
    fun `throws if executed on a new time entry`() = runBlockingTest {
        val timeEntry = EditableTimeEntry.empty(1)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(timeEntry))

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.DeleteButtonTapped,
            exception = TimeEntryShouldNotBeNewException::class.java
        )
    }

    @Test
    fun `throws if executed on a running time entry`() = runBlockingTest {
        val timeEntry = createTimeEntry(1, duration = null, startTime = OffsetDateTime.now())
        val editableTimeEntry = EditableTimeEntry.fromSingle(timeEntry)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry))

        reducer.testReduceException(
            initialState = initialState,
            action = ContextualMenuAction.DeleteButtonTapped,
            exception = TimeEntryShouldNotBeRunningException::class.java
        )
    }

    @Test
    fun `sets the selectedItem to null`() = runBlockingTest {
        val timeEntry = createTimeEntry(1, startTime = OffsetDateTime.now().minusMinutes(3), duration = Duration.ofMinutes(3))
        val editableTimeEntry = EditableTimeEntry.fromSingle(timeEntry)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry))

        reducer.testReduceState(initialState, ContextualMenuAction.DeleteButtonTapped) { state ->
            state shouldBe initialState.copy(selectedItem = null)
        }
    }

    @Test
    fun `returns an effect to delete the time entry`() = runBlockingTest {
        val timeEntry = createTimeEntry(1, startTime = OffsetDateTime.now().minusMinutes(3), duration = Duration.ofMinutes(3))
        val editableTimeEntry = EditableTimeEntry.fromSingle(timeEntry)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry))

        reducer.testReduceEffects(
            initialState = initialState,
            action = ContextualMenuAction.DeleteButtonTapped
        ) { effects -> effects.single().shouldEmitTimeEntryAction<ContextualMenuAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry> {
            it.id shouldBe timeEntry.id
        } }
    }
}