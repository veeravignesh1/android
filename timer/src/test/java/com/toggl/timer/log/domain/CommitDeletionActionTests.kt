package com.toggl.timer.log.domain

import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.shouldEmitTimeEntryAction
import com.toggl.timer.common.testReduce
import com.toggl.timer.common.toMutableValue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("CommitDeletionAction tests")
class CommitDeletionActionTests : CoroutineTest() {
    val repository = mockk<TimeEntryRepository>()
    val reducer = TimeEntriesLogReducer()

    @Test
    fun `The CommitDeletion action should do nothing if there are no ids pending deletion`() = runBlockingTest {
        val initialState = createInitialState(entriesPendingDeletion = setOf())
        reducer.testReduce(
            initialState,
            TimeEntriesLogAction.CommitDeletion(listOf())
        ) { state, effects ->
            state shouldBe initialState
            effects.shouldBeEmpty()
        }
    }

    @Test
    fun `The CommitDeletion action should do nothing if the ids pending deletion in action don't match those in state`() =
        runBlockingTest {
            val initialState = createInitialState(entriesPendingDeletion = setOf())
            reducer.testReduce(
                initialState,
                TimeEntriesLogAction.CommitDeletion(listOf())
            ) { state, effects ->
                state shouldBe initialState
                effects.shouldBeEmpty()
            }
        }

    @Test
    fun `The CommitDeletion action should do nothing if the ids pending deletion in action are a subset of those in state`() =
        runBlockingTest {
            val initialState = createInitialState(entriesPendingDeletion = setOf())
            reducer.testReduce(
                initialState,
                TimeEntriesLogAction.CommitDeletion(listOf())
            ) { state, effects ->
                state shouldBe initialState
                effects.shouldBeEmpty()
            }
        }

    @Test
    fun `The CommitDeletion action should do nothing if the ids pending deletion in action are a superset of those in state`() =
        runBlockingTest {
            val initialState = createInitialState(entriesPendingDeletion = setOf())
            reducer.testReduce(
                initialState,
                TimeEntriesLogAction.CommitDeletion(listOf())
            ) { state, effects ->
                state shouldBe initialState
                effects.shouldBeEmpty()
            }
        }

    @Test
    fun `The CommitDeletion action "should delete nothing but clear the pending list if the time entries are not in state`() =
        runBlockingTest {
            val initialState = createInitialState(entriesPendingDeletion = setOf(1, 2, 3))

            reducer.testReduce(
                initialState,
                TimeEntriesLogAction.CommitDeletion(listOf(1, 2, 3))
            ) { state, effects ->
                state shouldBe initialState.copy(entriesPendingDeletion = setOf())
                effects shouldHaveSize 3
                effects.forEach {
                    it.shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>()
                }
            }
        }

    @Test
    fun `The CommitDeletion action should delete time entries, clear the pending list and optimistically update the time entries list that were deleted`() =
        runBlockingTest {
            val te1 = createTimeEntry(1)
            val te2 = createTimeEntry(2)
            val te3 = createTimeEntry(3)
            val initialState = createInitialState(
                timeEntries = listOf(te1, te2, te3),
                entriesPendingDeletion = setOf(1, 2, 3)
            )
            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            coEvery { repository.deleteTimeEntry(any()) } returns mockk()
            val effect = reducer.reduce(
                mutableValue,
                TimeEntriesLogAction.CommitDeletion(listOf(1, 2, 3))
            )

            state.entriesPendingDeletion shouldBe setOf()

            state.timeEntries shouldBe listOf(
                te1.copy(isDeleted = true),
                te2.copy(isDeleted = true),
                te3.copy(isDeleted = true)
            ).associateBy { it.id }

            effect.forEach {
                it.shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>()
            }
        }
}
