package com.toggl.timer.log.domain

import com.toggl.common.feature.timeentry.exceptions.TimeEntryDoesNotExistException
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.toMutableValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TimeEntryTappedActionTests : CoroutineTest() {
    val reducer = TimeEntriesLogReducer()
    val testTe = createTimeEntry(1, "test")

    @Test
    fun `The TimeEntryTapped action should thrown when there are no time entries with the matching id`() = runBlockingTest {
        val initialState = createInitialState(listOf(testTe))
        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        shouldThrow<TimeEntryDoesNotExistException> {
            reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryTapped(2))
        }
    }

    @Test
    fun `The TimeEntryTapped action should thrown when there are no time entries at all`() = runBlockingTest {
        val initialState = createInitialState()
        checkAll { id: Long ->
            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            shouldThrow<TimeEntryDoesNotExistException> {
                reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryTapped(id))
            }
        }
    }

    @Test
    fun `The TimeEntryTapped action should set the editing time entry property when the time entry exists`() =
        runBlockingTest {
            val initialState = createInitialState(listOf(testTe))

            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryTapped(1))
            state.editableTimeEntry!!.ids.single() shouldBe testTe.id
        }
}
