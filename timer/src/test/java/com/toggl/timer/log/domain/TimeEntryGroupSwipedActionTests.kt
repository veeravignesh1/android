package com.toggl.timer.log.domain

import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.exceptions.TimeEntryDoesNotExistException
import com.toggl.models.common.SwipeDirection
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.shouldEmitTimeEntryAction
import com.toggl.timer.common.testReduceEffects
import com.toggl.timer.common.toMutableValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TimeEntryGroupSwipedActionTests : CoroutineTest() {
    val entriesInDatabase = listOf(createTimeEntry(1, "test"), createTimeEntry(2, "test"))
    val reducer = TimeEntriesLogReducer()

    @Test
    fun `The TimeEntryGroupSwiped action, when swiping right, should continue the swiped time entries`() = runBlockingTest {
        val initialState = createInitialState(entriesInDatabase)
        reducer.testReduceEffects(
            initialState,
            TimeEntriesLogAction.TimeEntryGroupSwiped(listOf(1, 2), SwipeDirection.Right)
        ) { effects ->
            effects.shouldBeSingleton()
            effects.first()
                .shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.ContinueTimeEntry>()
        }
    }

    @Test
    fun `The TimeEntryGroupSwiped action, when swiping right, should throw when there are no matching TEs`() {
        val initialState = createInitialState(entriesInDatabase)
        var state = initialState
        val mutableValue = state.toMutableValue { state = it }
        val action = TimeEntriesLogAction.TimeEntryGroupSwiped(listOf(2, 3), SwipeDirection.Right)

        shouldThrow<TimeEntryDoesNotExistException> {
            reducer.reduce(mutableValue, action)
        }
    }

    @Test
    fun `The TimeEntryGroupSwiped action, when swiping left, when there already pending entries for deletion should emit and optimistically update the time entries list that were deleted`() =
        runBlockingTest {
            val timeEntries = (1L..10L).map { createTimeEntry(it, "testing") }
            val timeEntriesToSwipe = timeEntries.take(4)

            val initialState = createInitialState(timeEntries, entriesPendingDeletion = setOf(8, 9, 11))
            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            val action =
                TimeEntriesLogAction.TimeEntryGroupSwiped(timeEntriesToSwipe.map { it.id }, SwipeDirection.Left)

            val effectActions = reducer.reduce(mutableValue, action)
            val deleteTimeEntryEffects = effectActions.dropLast(1)

            deleteTimeEntryEffects shouldHaveSize 2
            deleteTimeEntryEffects.first()
                .shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>() {
                    it.id shouldBeExactly 8
                }

            deleteTimeEntryEffects[1].shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>() {
                it.id shouldBeExactly 9
            }
            val expectedTimeEntries = timeEntries.take(7) +
                timeEntries.drop(7).take(2).map { it.copy(isDeleted = true) } +
                timeEntries.last()
            state.timeEntries shouldBe expectedTimeEntries.associateBy { it.id }

            val waitForUndoEffect = effectActions.last()

            waitForUndoEffect.shouldBeTypeOf<WaitForUndoEffect>()
            val executedUndo = waitForUndoEffect.execute()
            executedUndo shouldBe TimeEntriesLogAction.CommitDeletion(listOf(1L, 2L, 3L, 4L))
        }

    @Test
    fun `The TimeEntryGroupSwiped action, when swiping left, should just put the swiped TEs to pending deletion in state in case there's nothing pending deletion`() =
        runBlockingTest {
            val timeEntries = (1L..10L).map { createTimeEntry(it, "testing") }
            val timeEntriesToSwipe = timeEntries.take(4)

            val initialState = createInitialState(timeEntries)
            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            val action =
                TimeEntriesLogAction.TimeEntryGroupSwiped(timeEntriesToSwipe.map { it.id }, SwipeDirection.Left)

            val effectActions = reducer.reduce(mutableValue, action)
            val waitForUndoEffect = effectActions[0]

            effectActions.size shouldBe 1
            state.entriesPendingDeletion shouldBe setOf(1L, 2L, 3L, 4L)
            waitForUndoEffect.shouldBeTypeOf<WaitForUndoEffect>()
            val executedUndo = waitForUndoEffect.execute()
            executedUndo shouldBe TimeEntriesLogAction.CommitDeletion(listOf(1L, 2L, 3L, 4L))
        }
}
