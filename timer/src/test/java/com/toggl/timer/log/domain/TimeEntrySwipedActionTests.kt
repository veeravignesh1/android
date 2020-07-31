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
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class TimeEntrySwipedActionTests : CoroutineTest() {
    val entryInDatabase = createTimeEntry(1, "test")
    val reducer = TimeEntriesLogReducer()

    @Test
    fun `The TimeEntrySwiped action, when swiping right, should continue the swiped time entry`() = runBlockingTest {
        val initialState = createInitialState(listOf(entryInDatabase))
        reducer.testReduceEffects(
            initialState,
            TimeEntriesLogAction.TimeEntrySwiped(1, SwipeDirection.Right)
        ) { effect ->
            effect.shouldBeSingleton()
            effect.first()
                .shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.ContinueTimeEntry>()
        }
    }

    @Test
    fun `The TimeEntrySwiped action, when swiping right, should throw if there are no TEs matching in state`() =
        runBlockingTest {
            val initialState = createInitialState(listOf(entryInDatabase))
            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            val action = TimeEntriesLogAction.TimeEntrySwiped(1337, SwipeDirection.Right)

            shouldThrow<TimeEntryDoesNotExistException> {
                reducer.reduce(mutableValue, action)
            }
        }

    @Test
    fun `The TimeEntrySwiped action, when swiping left, should delete TEs pending deletion (ignoring ids not in state) and put the swiped TEs to pending deletion in state`() =
        runBlockingTest {
            val initialState = createInitialState(
                listOf(entryInDatabase, entryInDatabase.copy(id = 2)),
                entriesPendingDeletion = setOf(1, 4)
            )

            reducer.testReduceEffects(
                initialState,
                TimeEntriesLogAction.TimeEntrySwiped(2, SwipeDirection.Left)
            ) { effects ->
                effects shouldHaveSize 2
                effects.first()
                    .shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>()
            }
        }

    @Test
    fun `The TimeEntrySwiped action, when swiping left, should just put the swiped TEs to pending deletion in state if there's nothing pending deletion`() =
        runBlockingTest {
            val initialState = createInitialState(listOf(entryInDatabase, entryInDatabase.copy(id = 2)))
            var state = initialState
            val mutableValue = state.toMutableValue { state = it }
            val action = TimeEntriesLogAction.TimeEntrySwiped(2, SwipeDirection.Left)

            val effectActions = reducer.reduce(mutableValue, action)

            effectActions.size shouldBe 1
            state.entriesPendingDeletion shouldBe setOf(2L)
            effectActions[0].shouldBeTypeOf<WaitForUndoEffect>()
            val executedUndo = effectActions[0].execute()
            executedUndo shouldBe TimeEntriesLogAction.CommitDeletion(listOf(2))
        }
}
