package com.toggl.architecture.core

import com.toggl.architecture.extensions.compose
import com.toggl.architecture.extensions.map
import com.toggl.architecture.extensions.noEffect

typealias ReduceFunction<State, Action> =
        (SettableValue<State>, Action) -> Effect<Action>

class Reducer<State, Action>(
    val reduce: ReduceFunction<State, Action>
)

fun <State, Action> combine(vararg reducers: Reducer<State, Action>):
    Reducer<State, Action> =
    Reducer { state, action ->
        reducers.map { it.reduce(state, action) }
            .compose()
    }

fun <LocalState, GlobalState, LocalAction, GlobalAction>
    Reducer<LocalState, LocalAction>.pullback(
        mapToLocalState: (GlobalState) -> LocalState,
        mapToLocalAction: (GlobalAction) -> LocalAction?,
        mapToGlobalAction: (LocalAction) -> GlobalAction,
        mapToGlobalState: (GlobalState, LocalState) -> GlobalState
    ): Reducer<GlobalState, GlobalAction> =
    Reducer { globalState, globalAction ->
        val localAction = mapToLocalAction(globalAction)
            ?: return@Reducer noEffect()

        reduce(globalState.map(mapToLocalState, mapToGlobalState), localAction)
            .map { it?.run(mapToGlobalAction) }
    }
