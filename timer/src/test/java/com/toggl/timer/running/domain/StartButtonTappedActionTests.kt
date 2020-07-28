package com.toggl.timer.running.domain

import com.toggl.timer.common.FreeCoroutineSpec

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StartButtonTappedActionTests : FreeCoroutineSpec() {
    // init {
    //     val timeService = mockk<TimeService> { every { now() } returns OffsetDateTime.MAX }
    //     val reducer = RunningTimeEntryReducer(timeService)
    //     val initialState = createInitialState()
    //
    //     "The StartButtonTapped action" - {
    //         reducer.testReduce(
    //             initialState = initialState,
    //             action = RunningTimeEntryAction.StartButtonTapped
    //         ) { state, effect ->
    //             "shouldn't change the state" {
    //                 assertThat(state).isEqualTo(initialState)
    //             }
    //             "should emit StartTimeEntry effect" {
    //                 assertThat(effect).hasSize(1)
    //                 effect.first()
    //                     .shouldEmitTimeEntryAction<RunningTimeEntryAction.TimeEntryHandling, TimeEntryAction.StartTimeEntry>()
    //             }
    //         }
    //     }
    // }
}