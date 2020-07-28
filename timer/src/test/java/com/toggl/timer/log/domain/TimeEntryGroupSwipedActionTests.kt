package com.toggl.timer.log.domain

import com.toggl.timer.common.FreeCoroutineSpec

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TimeEntryGroupSwipedActionTests : FreeCoroutineSpec() {
    // init {
    //     val entriesInDatabase = listOf(createTimeEntry(1, "test"), createTimeEntry(2, "test"))
    //     val reducer = TimeEntriesLogReducer()
    //
    //     "The TimeEntryGroupSwiped action" - {
    //
    //         "when swiping right" - {
    //             "should continue the swiped time entries" {
    //                 val initialState = createInitialState(entriesInDatabase)
    //                 reducer.testReduceEffects(
    //                     initialState,
    //                     TimeEntriesLogAction.TimeEntryGroupSwiped(listOf(1, 2), SwipeDirection.Right)
    //                 ) { effects ->
    //                     assertThat(effects).hasSize(1)
    //                     effects.first().shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.ContinueTimeEntry>()
    //                 }
    //             }
    //
    //             "should throw when there are no matching TEs" {
    //                 val initialState = createInitialState(entriesInDatabase)
    //                 var state = initialState
    //                 val mutableValue = state.toMutableValue { state = it }
    //                 val action = TimeEntriesLogAction.TimeEntryGroupSwiped(listOf(2, 3), SwipeDirection.Right)
    //
    //                 shouldThrow<TimeEntryDoesNotExistException> {
    //                     reducer.reduce(mutableValue, action)
    //                 }
    //             }
    //         }
    //
    //         "when swiping left" - {
    //             "when there already pending entries for deletion should" - {
    //                 val timeEntries = (1L..10L).map { createTimeEntry(it, "testing") }
    //                 val timeEntriesToSwipe = timeEntries.take(4)
    //
    //                 val initialState = createInitialState(timeEntries, entriesPendingDeletion = setOf(8, 9, 11))
    //                 var state = initialState
    //                 val mutableValue = state.toMutableValue { state = it }
    //                 val action =
    //                     TimeEntriesLogAction.TimeEntryGroupSwiped(timeEntriesToSwipe.map { it.id }, SwipeDirection.Left)
    //
    //                 val effectActions = reducer.reduce(mutableValue, action)
    //                 val deleteTimeEntryEffects = effectActions.dropLast(1)
    //
    //                 deleteTimeEntryEffects shouldHaveSize 2
    //                 deleteTimeEntryEffects.first().shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>() {
    //                     it.id shouldBeExactly 8
    //                 }
    //
    //                 deleteTimeEntryEffects[1].shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>() {
    //                     it.id shouldBeExactly 9
    //                 }
    //
    //                 "and optimistically update the time entries list that were deleted" - {
    //                     val expectedTimeEntries = timeEntries.take(7) +
    //                         timeEntries.drop(7).take(2).map { it.copy(isDeleted = true) } +
    //                         timeEntries.last()
    //                     assertThat(state.timeEntries).isEqualTo(expectedTimeEntries.associateBy { it.id })
    //                 }
    //
    //                 val waitForUndoEffect = effectActions.last()
    //
    //                 assertThat(waitForUndoEffect).isInstanceOf(WaitForUndoEffect::class.java)
    //                 runBlockingTest {
    //                     val executedUndo = waitForUndoEffect.execute()
    //                     assertThat(executedUndo).isEqualTo(TimeEntriesLogAction.CommitDeletion(listOf(1L, 2L, 3L, 4L)))
    //                 }
    //             }
    //
    //             "should just put the swiped TEs to pending deletion in state in case there's nothing pending deletion" {
    //                 val timeEntries = (1L..10L).map { createTimeEntry(it, "testing") }
    //                 val timeEntriesToSwipe = timeEntries.take(4)
    //
    //                 val initialState = createInitialState(timeEntries)
    //                 var state = initialState
    //                 val mutableValue = state.toMutableValue { state = it }
    //                 val action =
    //                     TimeEntriesLogAction.TimeEntryGroupSwiped(timeEntriesToSwipe.map { it.id }, SwipeDirection.Left)
    //
    //                 val effectActions = reducer.reduce(mutableValue, action)
    //                 val waitForUndoEffect = effectActions[0]
    //
    //                 assertThat(effectActions.size).isEqualTo(1)
    //                 assertThat(state.entriesPendingDeletion).isEqualTo(setOf(1L, 2L, 3L, 4L))
    //                 assertThat(waitForUndoEffect).isInstanceOf(WaitForUndoEffect::class.java)
    //                 runBlockingTest {
    //                     val executedUndo = waitForUndoEffect.execute()
    //                     assertThat(executedUndo).isEqualTo(TimeEntriesLogAction.CommitDeletion(listOf(1L, 2L, 3L, 4L)))
    //                 }
    //             }
    //         }
    //     }
    // }
}