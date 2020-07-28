package com.toggl.timer.log.domain

import com.toggl.timer.common.FreeCoroutineSpec

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CommitDeletionActionTests : FreeCoroutineSpec() {
    // init {
    //     val repository = mockk<TimeEntryRepository>()
    //     val reducer = TimeEntriesLogReducer()
    //
    //     "The CommitDeletion action" - {
    //         "should do nothing if" - {
    //             "there are no ids pending deletion" {
    //                 val initialState = createInitialState(entriesPendingDeletion = setOf())
    //                 reducer.testReduce(
    //                     initialState,
    //                     TimeEntriesLogAction.CommitDeletion(listOf())
    //                 ) { state, effects ->
    //                     assertThat(state).isEqualTo(initialState)
    //                     assertThat(effects).isEmpty()
    //                 }
    //             }
    //
    //             "the ids pending deletion in action don't match those in state" {
    //                 val initialState = createInitialState(entriesPendingDeletion = setOf(1, 2, 3))
    //                 reducer.testReduce(
    //                     initialState,
    //                     TimeEntriesLogAction.CommitDeletion(listOf(4, 5, 1337))
    //                 ) { state, effects ->
    //                     assertThat(state).isEqualTo(initialState)
    //                     assertThat(effects).isEmpty()
    //                 }
    //             }
    //
    //             "the ids pending deletion in action are a subset of those in state" {
    //                 val initialState = createInitialState(entriesPendingDeletion = setOf(1, 2, 3))
    //                 var state = initialState
    //                 val mutableValue = state.toMutableValue { state = it }
    //
    //                 val effect = reducer.reduce(
    //                     mutableValue,
    //                     TimeEntriesLogAction.CommitDeletion(listOf(1, 3))
    //                 )
    //
    //                 assertThat(state).isEqualTo(initialState)
    //                 assertThat(effect).isEqualTo(noEffect())
    //             }
    //
    //             "the ids pending deletion in action are a superset of those in state" {
    //                 val initialState = createInitialState(entriesPendingDeletion = setOf(1, 2, 3))
    //                 var state = initialState
    //                 val mutableValue = state.toMutableValue { state = it }
    //
    //                 val effect = reducer.reduce(
    //                     mutableValue,
    //                     TimeEntriesLogAction.CommitDeletion(listOf(1, 2, 3, 1337))
    //                 )
    //
    //                 assertThat(state).isEqualTo(initialState)
    //                 assertThat(effect).isEqualTo(noEffect())
    //             }
    //         }
    //
    //         "should delete nothing but clear the pending list if the time entries are not in state" - {
    //             val initialState = createInitialState(entriesPendingDeletion = setOf(1, 2, 3))
    //
    //             reducer.testReduce(
    //                 initialState,
    //                 TimeEntriesLogAction.CommitDeletion(listOf(1, 2, 3))
    //             ) { state, effects ->
    //                 assertThat(state).isEqualTo(initialState.copy(entriesPendingDeletion = setOf()))
    //                 effects shouldHaveSize 3
    //                 effects.forEach {
    //                     it.shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>()
    //                 }
    //             }
    //         }
    //
    //         "should delete time entries" - {
    //             val te1 = createTimeEntry(1)
    //             val te2 = createTimeEntry(2)
    //             val te3 = createTimeEntry(3)
    //             val initialState = createInitialState(
    //                 timeEntries = listOf(te1, te2, te3),
    //                 entriesPendingDeletion = setOf(1, 2, 3)
    //             )
    //             var state = initialState
    //             val mutableValue = state.toMutableValue { state = it }
    //             coEvery { repository.deleteTimeEntry(any()) } returns mockk()
    //             val effect = reducer.reduce(
    //                 mutableValue,
    //                 TimeEntriesLogAction.CommitDeletion(listOf(1, 2, 3))
    //             )
    //
    //             "clear the pending list and" - {
    //                 assertThat(state.entriesPendingDeletion).isEqualTo(setOf())
    //             }
    //
    //             "optimistically update the time entries list that were deleted" - {
    //                 assertThat(state.timeEntries).isEqualTo(listOf()
    //                     te1.copy(isDeleted = true),
    //                     te2.copy(isDeleted = true),
    //                     te3.copy(isDeleted = true)
    //                 ).associateBy { it.id }
    //             }
    //
    //             effect.forEach {
    //                 it.shouldEmitTimeEntryAction<TimeEntriesLogAction.TimeEntryHandling, TimeEntryAction.DeleteTimeEntry>()
    //             }
    //         }
    //     }
    // }
}