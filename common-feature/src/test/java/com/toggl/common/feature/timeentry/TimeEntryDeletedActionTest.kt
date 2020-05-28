package com.toggl.common.feature.timeentry

import com.toggl.common.feature.common.CoroutineTest
import com.toggl.common.feature.common.createTimeEntry
import com.toggl.common.feature.common.testReduceState
import com.toggl.repository.interfaces.TimeEntryRepository
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The TimeEntryDeletedAction")
class TimeEntryDeletedActionTest : CoroutineTest() {

    private val repository = mockk<TimeEntryRepository>()
    private val timeEntries = (1L..10L).map { createTimeEntry(it, "test") }.associateBy { it.id }
    private val initialState = TimeEntryState(timeEntries = timeEntries)
    private val reducer = TimeEntryReducer(repository, mockk(), dispatcherProvider)

    @Test
    fun `should remove deleted time entry from the state`() = runBlockingTest {
        val timeEntry = timeEntries[1L]!!
        reducer.testReduceState(
            initialState,
            TimeEntryAction.TimeEntryDeleted(timeEntry)
        ) { state ->
            state.timeEntries[timeEntry.id] shouldBe null
        }
    }

    @Test
    fun `shouldn't change the state when deleted time entry doesn't exist`() = runBlockingTest {
        val nonExistingTimeEntry = createTimeEntry(321, "I don't exist")
        reducer.testReduceState(
            initialState,
            TimeEntryAction.TimeEntryDeleted(nonExistingTimeEntry)
        ) { state ->
            state shouldBe initialState
        }
    }
}
