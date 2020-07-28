package com.toggl.domain.reducers

import com.toggl.architecture.core.MutableValue
import com.toggl.domain.AppState

class AnalyticsReducerTests
//     : FreeSpec({
//     mockkStatic("com.toggl.domain.reducers.AnalyticsReducerKt")
//     val analyticsService = mockk<AnalyticsService>(relaxed = true)
//     val analyticsReducer = AnalyticsReducer(analyticsService)
//
//     "The AnalyticsReducer's reduce function " - {
//         val initialState = AppState()
//
//         "does not change the state" - {
//             var state = initialState
//             val mutableValue = state.toMutableValue { state = it }
//             val appAction = mockk<AppAction>()
//
//             analyticsReducer.reduce(mutableValue, appAction)
//
//             assertThat(initialState).isEqualTo(state)
//         }
//
//         "does not produce any effect" - {
//             var state = initialState
//             val mutableValue = state.toMutableValue { state = it }
//             val appAction = mockk<AppAction>()
//
//             val effect = analyticsReducer.reduce(mutableValue, appAction)
//
//             assertThat(effect).isEqualTo(noEffect())
//         }
//
//         "does not call the AnalyticsService's track method if the event is null" - {
//             var state = initialState
//             val mutableValue = state.toMutableValue { state = it }
//             val appAction = AppAction.Loading(LoadingAction.StartLoading)
//
//             analyticsReducer.reduce(mutableValue, appAction)
//
//             verify { analyticsService wasNot Called }
//         }
//
//         "calls the AnalyticsService's track method if the event is not null" - {
//             var state = initialState
//             val mutableValue = state.toMutableValue { state = it }
//             val appAction = AppAction.Timer(TimerAction.TimeEntriesLog(TimeEntriesLogAction.UndoButtonTapped))
//             val expectedEvent = Event.undoTapped()
//
//             analyticsReducer.reduce(mutableValue, appAction)
//
//             verify(exactly = 1) { analyticsService.track(expectedEvent) }
//         }
//     }
// })

fun AppState.toMutableValue(setFunction: (AppState) -> Unit) =
    MutableValue({ this }, setFunction)
