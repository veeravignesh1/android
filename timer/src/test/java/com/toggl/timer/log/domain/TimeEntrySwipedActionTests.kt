package com.toggl.timer.log.domain

import com.toggl.models.common.SwipeDirection
import com.toggl.repository.interfaces.StartTimeEntryResult
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.FreeCoroutineSpec
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.toMutableValue
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
class TimeEntrySwipedActionTests : FreeCoroutineSpec() {
    init {
        val repository = mockk<TimeEntryRepository>()
        val entryInDatabase = createTimeEntry(1, "test")
        val entryToBeStarted = createTimeEntry(2, "test")
        coEvery { repository.startTimeEntry(1, "test") } returns StartTimeEntryResult(
            entryToBeStarted,
            null
        )
        coEvery { repository.deleteTimeEntry(entryInDatabase) } returns entryInDatabase.copy(
            isDeleted = true
        )
        val reducer = TimeEntriesLogReducer(repository, dispatcherProvider)

        "The TimeEntrySwiped action" - {

            "when swiping right" - {
                "should continue the swiped time entry" {
                    val initialState = createInitialState(listOf(entryInDatabase))
                    var state = initialState
                    val mutableValue = state.toMutableValue { state = it }
                    val action = TimeEntriesLogAction.TimeEntrySwiped(1, SwipeDirection.Right)
                    val effectAction = reducer.reduce(
                        mutableValue,
                        action
                    ).single().execute() as TimeEntriesLogAction.TimeEntryStarted
                    val startedTimeEntry = effectAction.startedTimeEntry
                    startedTimeEntry shouldBe entryToBeStarted
                }
            }

            "should throw if there are no TEs matching in state" {
                val initialState = createInitialState(listOf(entryInDatabase))
                var state = initialState
                val mutableValue = state.toMutableValue { state = it }
                val action = TimeEntriesLogAction.TimeEntrySwiped(1337, SwipeDirection.Right)

                shouldThrow<IllegalStateException> {
                    reducer.reduce(mutableValue, action)
                }
            }
        }

        "when swiping left" - {
            "should delete TEs pending deletion (ignoring ids not in state) and put the swiped TEs to pending deletion in state" {
                val initialState = createInitialState(
                    listOf(entryInDatabase, entryInDatabase.copy(id = 2)),
                    entriesPendingDeletion = setOf(1, 4)
                )
                var state = initialState
                val mutableValue = state.toMutableValue { state = it }
                val action = TimeEntriesLogAction.TimeEntrySwiped(2, SwipeDirection.Left)

                val effectActions = reducer.reduce(mutableValue, action)
                val deletedEntry = effectActions[0].execute() as TimeEntriesLogAction.TimeEntryDeleted

                deletedEntry.deletedTimeEntry shouldBe entryInDatabase.copy(isDeleted = true)
                state.entriesPendingDeletion shouldBe setOf(2L)
                effectActions[1].shouldBeTypeOf<WaitForUndoEffect>()
                runBlockingTest {
                    val executedUndo = effectActions[1].execute()
                    executedUndo shouldBe TimeEntriesLogAction.CommitDeletion(listOf(2))
                }
            }

            "should just put the swiped TEs to pending deletion in state if there's nothing pending deletion" {
                val initialState = createInitialState(listOf(entryInDatabase, entryInDatabase.copy(id = 2)))
                var state = initialState
                val mutableValue = state.toMutableValue { state = it }
                val action = TimeEntriesLogAction.TimeEntrySwiped(2, SwipeDirection.Left)

                val effectActions = reducer.reduce(mutableValue, action)

                effectActions.size shouldBe 1
                state.entriesPendingDeletion shouldBe setOf(2L)
                effectActions[0].shouldBeTypeOf<WaitForUndoEffect>()
                runBlockingTest {
                    val executedUndo = effectActions[0].execute()
                    executedUndo shouldBe TimeEntriesLogAction.CommitDeletion(listOf(2))
                }
            }
        }
    }
}