package com.toggl.timer.log.domain

import com.toggl.timer.common.FreeCoroutineSpec

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TimeEntrySwipedActionTests : FreeCoroutineSpec() {
    // init {
    //     val entryInDatabase = createTimeEntry(1, "test")
    //     val reducer = TimeEntriesLogReducer()
    //
    //     "The TimeEntrySwiped action" - {
    //
    //         "when swiping right" - {
    //             "should continue the swiped time entry" {
    //                 val initialState = createInitialState(listOf(entryInDatabase))
    //                 reducer.testReduceEffects(
    //                     initialState,
    //                     TimeEntriesLogAction.TimeEntrySwiped(1, SwipeDirection.Right)
    //                 ) { effect ->
    //                     assertThat(effect).hasSize(1)
    //                     effect.first().shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.ContinueTimeEntry>()
    //                 }
    //             }
    //         }
    //
    //         "should throw if there are no TEs matching in state" {
    //             val initialState = createInitialState(listOf(entryInDatabase))
    //             var state = initialState
    //             val mutableValue = state.toMutableValue { state = it }
    //             val action = TimeEntriesLogAction.TimeEntrySwiped(1337, SwipeDirection.Right)
    //
    //             shouldThrow<TimeEntryDoesNotExistException> {
    //                 reducer.reduce(mutableValue, action)
    //             }
    //         }
    //     }
    //
    //     "when swiping left" - {
    //         "should delete TEs pending deletion (ignoring ids not in state) and put the swiped TEs to pending deletion in state" {
    //             val initialState = createInitialState(
    //                 listOf(entryInDatabase, entryInDatabase.copy(id = 2)),
    //                 entriesPendingDeletion = setOf(1, 4)
    //             )
    //
    //             reducer.testReduceEffects(
    //                 initialState,
    //                 TimeEntriesLogAction.TimeEntrySwiped(2, SwipeDirection.Left)
    //             ) { effects ->
    //                 effects shouldHaveSize 2
    //                 effects.first()
    //                     .shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>()
    //             }
    //         }
    //
    //         "should just put the swiped TEs to pending deletion in state if there's nothing pending deletion" {
    //             val initialState = createInitialState(listOf(entryInDatabase, entryInDatabase.copy(id = 2)))
    //             var state = initialState
    //             val mutableValue = state.toMutableValue { state = it }
    //             val action = TimeEntriesLogAction.TimeEntrySwiped(2, SwipeDirection.Left)
    //
    //             val effectActions = reducer.reduce(mutableValue, action)
    //
    //             assertThat(effectActions.size).isEqualTo(1)
    //             assertThat(state.entriesPendingDeletion).isEqualTo(setOf(2L))
    //             assertThat(effectActions[0]).isInstanceOf(WaitForUndoEffect::class.java)
    //             runBlockingTest {
    //                 val executedUndo = effectActions[0].execute()
    //                 assertThat(executedUndo).isEqualTo(TimeEntriesLogAction.CommitDeletion(listOf(2)))
    //             }
    //         }
    //     }
    // }
}