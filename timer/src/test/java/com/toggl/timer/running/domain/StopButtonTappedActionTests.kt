package com.toggl.timer.running.domain

import com.toggl.timer.common.FreeCoroutineSpec

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StopButtonTappedActionTests : FreeCoroutineSpec() {
    // init {
    //     val workspace = mockk<Workspace>()
    //     val timeService = mockk<TimeService>()
    //     val reducer = RunningTimeEntryReducer(timeService)
    //     val initialState = createInitialState()
    //
    //     coEvery { workspace.id } returns 1
    //
    //     "The StopButtonTapped action" - {
    //         reducer.testReduce(
    //             initialState = initialState,
    //             action = RunningTimeEntryAction.StopButtonTapped
    //         ) { state, effect ->
    //             "shouldn't change the state" {
    //                 assertThat(state).isEqualTo(initialState)
    //             }
    //             "should emit StopTimeEntry effect" {
    //                 assertThat(effect).hasSize(1)
    //                 effect.single()
    //                     .shouldEmitTimeEntryAction<RunningTimeEntryAction.TimeEntryHandling, TimeEntryAction.StopRunningTimeEntry>()
    //             }
    //         }
    //     }
    // }
}