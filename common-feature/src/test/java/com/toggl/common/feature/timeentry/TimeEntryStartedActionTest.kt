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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.threeten.bp.Duration

@ExperimentalCoroutinesApi
@DisplayName("The TimeEntryStartedAction")
class TimeEntryStartedActionTest : CoroutineTest() {

    private val repository = mockk<TimeEntryRepository>()
    private val reducer = TimeEntryReducer(repository, mockk(), dispatcherProvider)
    val timeEntries = mapOf(
        1L to createTimeEntry(1, "first", duration = Duration.ofHours(1)),
        2L to createTimeEntry(2, "second", duration = null)
    )
    private val initialState = TimeEntryState(timeEntries)
    val started = createTimeEntry(3, "started", duration = null)
    val stopped = timeEntries[2L]!!.copy(duration = Duration.ofHours(2))

    @Nested
    @DisplayName("with stopped time entry")
    inner class WithStoppedTimeEntry {
        val action = TimeEntryAction.TimeEntryStarted(started, stopped)

        @Test
        fun `should start time entry`() = runBlockingTest {
            reducer.testReduceState(initialState, action) { state ->
                state.timeEntries.shouldContain(3L to started)
            }
        }

        @Test
        fun `should stop stopped time entry`() = runBlockingTest {
            reducer.testReduceState(initialState, action) { state ->
                state.timeEntries.shouldContain(2L to stopped)
            }
        }

        @Test
        fun `shouldn't return any effect`() = runBlockingTest {
            reducer.testReduceNoEffects(initialState, action)
        }
    }

    @Nested
    @DisplayName("without stopping any time entry")
    inner class WithoutStoppedTimeEntry {
        val action = TimeEntryAction.TimeEntryStarted(started, null)

        @Test
        fun `should start started time entry`() = runBlockingTest {
            reducer.testReduceState(initialState, action) { state ->
                state.timeEntries.shouldContain(3L to started)
            }
        }

        @Test
        fun `shouldn't change any other time entry than the started one`() = runBlockingTest {
            reducer.testReduceState(initialState, action) { state ->
                state.timeEntries.filterKeys { key -> key != 3L } shouldBe initialState.timeEntries.filterKeys { key -> key != 3L }
            }
        }

        @Test
        fun `shouldn't return any effect`() = runBlockingTest {
            reducer.testReduceNoEffects(initialState, action)
        }
    }
}
