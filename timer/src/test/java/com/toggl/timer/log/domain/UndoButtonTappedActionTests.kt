package com.toggl.timer.log.domain

import com.toggl.timer.common.FreeCoroutineSpec

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UndoButtonTappedActionTests : FreeCoroutineSpec() {
    // init {
    //     val reducer = TimeEntriesLogReducer()
    //
    //     "The UndoButtonTapped action" - {
    //         "clears entriesPendingDeletion if there are any" - {
    //             var initialState = createInitialState(entriesPendingDeletion = setOf(1, 5, 1337))
    //             val mutableValue = initialState.toMutableValue { initialState = it }
    //
    //             val effect = reducer.reduce(mutableValue, TimeEntriesLogAction.UndoButtonTapped)
    //
    //             assertThat(effect).isEqualTo(noEffect())
    //             assertThat(initialState.entriesPendingDeletion).isEqualTo(emptySet())
    //         }
    //         "keeps entriesPendingDeletion empty if they are empty" - {
    //             var initialState = createInitialState(entriesPendingDeletion = setOf())
    //             val mutableValue = initialState.toMutableValue { initialState = it }
    //
    //             val effect = reducer.reduce(mutableValue, TimeEntriesLogAction.UndoButtonTapped)
    //
    //             assertThat(effect).isEqualTo(noEffect())
    //             assertThat(initialState.entriesPendingDeletion).isEqualTo(emptySet())
    //         }
    //     }
    // }
}