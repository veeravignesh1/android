package com.toggl.common.feature.timeentry

import com.toggl.common.feature.common.CoroutineTest
import com.toggl.common.feature.common.createTimeEntry
import com.toggl.common.feature.common.testReduceNoEffects
import com.toggl.common.feature.common.testReduceState
import com.toggl.repository.interfaces.TimeEntryRepository
import io.kotlintest.matchers.maps.shouldContain
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The TimeEntryUpdatedAction")
class TimeEntryUpdatedActionTest : CoroutineTest() {

    private val timeEntries = listOf(
        createTimeEntry(1, "first"),
        createTimeEntry(2, "second"),
        createTimeEntry(3, "third")
    )
    private val initialState = TimeEntryState(timeEntries.associateBy { it.id })
    private val updated = timeEntries[1].copy(description = "second updated")
    private val repository = mockk<TimeEntryRepository>()
    private val reducer = TimeEntryReducer(repository, mockk(), dispatcherProvider)

    @Test
    fun `should update updated time entry`() = runBlockingTest {
        reducer.testReduceState(
            initialState = initialState,
            action = TimeEntryAction.TimeEntryUpdated(updated)
        ) { state -> state.timeEntries.shouldContain(2L to updated) }
    }

    @Test
    fun `shouldn't change any other time entry than the updated one`() = runBlockingTest {
        val expectedTimeEntries = initialState.timeEntries.filterKeys { key -> key != 2L }
        reducer.testReduceState(
            initialState = initialState,
            action = TimeEntryAction.TimeEntryUpdated(updated)
        ) { state -> state.timeEntries.filterKeys { key -> key != 2L } shouldBe expectedTimeEntries }
    }

    @Test
    fun `shouldn't return any effect`() = runBlockingTest {
        reducer.testReduceNoEffects(
            initialState = initialState,
            action = TimeEntryAction.TimeEntryUpdated(updated)
        )
    }
}
