package com.toggl.architecture.core

import com.toggl.architecture.extensions.map
import com.toggl.architecture.extensions.noEffect

interface Reducer<State, Action> {
    fun reduce(state: SettableValue<State>, action: Action): List<Effect<Action>>
}

interface HigherOrderReducer<State, Action> : Reducer<State, Action> {
    val innerReducer: Reducer<State, Action>
}

fun <State, Action> combine(vararg reducers: Reducer<State, Action>) =
    CombinedReducer(reducers.toList())

class CombinedReducer<State, Action>(private val reducers: List<Reducer<State, Action>>)
    : Reducer<State, Action> {
    override fun reduce(state: SettableValue<State>, action: Action): List<Effect<Action>> =
        reducers.flatMap { it.reduce(state, action) }
}

fun <LocalState, GlobalState, LocalAction, GlobalAction>
    Reducer<LocalState, LocalAction>.pullback(
        mapToLocalState: (GlobalState) -> LocalState,
        mapToLocalAction: (GlobalAction) -> LocalAction?,
        mapToGlobalState: (GlobalState, LocalState) -> GlobalState,
        mapToGlobalAction: (LocalAction) -> GlobalAction
    ): Reducer<GlobalState, GlobalAction> =
    PullbackReducer(this, mapToLocalState, mapToLocalAction, mapToGlobalState, mapToGlobalAction)

class PullbackReducer<LocalState, GlobalState, LocalAction, GlobalAction>(
    private val innerReducer: Reducer<LocalState, LocalAction>,
    private val mapToLocalState: (GlobalState) -> LocalState,
    private val mapToLocalAction: (GlobalAction) -> LocalAction?,
    private val mapToGlobalState: (GlobalState, LocalState) -> GlobalState,
    private val mapToGlobalAction: (LocalAction) -> GlobalAction
) : Reducer<GlobalState, GlobalAction> {
    override fun reduce(
        state: SettableValue<GlobalState>,
        action: GlobalAction
    ): List<Effect<GlobalAction>> {
        val localAction = mapToLocalAction(action)
            ?: return noEffect()

        return innerReducer
            .reduce(state.map(mapToLocalState, mapToGlobalState), localAction)
            .map { effect -> effect.map { action -> action?.run(mapToGlobalAction) } }
    }
}