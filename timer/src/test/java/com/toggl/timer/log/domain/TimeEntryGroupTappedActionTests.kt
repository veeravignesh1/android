package com.toggl.timer.log.domain

import com.toggl.common.feature.timeentry.exceptions.TimeEntryDoesNotExistException
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.toMutableValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.time.Duration

class TimeEntryGroupTappedActionTests : CoroutineTest() {

    val reducer = TimeEntriesLogReducer()
    val testTimeEntries = listOf(createTimeEntry(1, "test"), createTimeEntry(2, "test"))

    @Test
    fun `The TimeEntryGroupTapped action should thrown when there are no time entries with the matching id`() {
        val initialState = createInitialState(testTimeEntries)
        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        shouldThrow<TimeEntryDoesNotExistException> {
            reducer.reduce(
                mutableValue,
                TimeEntriesLogAction.TimeEntryGroupTapped(listOf(3))
            )
        }
    }

    @Test
    fun `The TimeEntryGroupTapped action should thrown when there are no time entries at all`() = runBlockingTest {
        val initialState = createInitialState()
        checkAll { id: Long ->
            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            shouldThrow<TimeEntryDoesNotExistException> {
                reducer.reduce(
                    mutableValue,
                    TimeEntriesLogAction.TimeEntryGroupTapped(listOf(id))
                )
            }
        }
    }

    @Test
    fun `The TimeEntryGroupTapped action should set the editing time entry property when the time entry exists`() {
        val initialState = createInitialState(testTimeEntries)

        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        val idsToEdit = listOf(1L, 2L)
        reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryGroupTapped(idsToEdit))
        state.editableTimeEntry!!.ids shouldBe idsToEdit
        state.editableTimeEntry!!.duration shouldBe Duration.ofMinutes(4)
    }
}
