package com.toggl.common.feature

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.combine
import com.toggl.architecture.core.isOrWraps
import com.toggl.architecture.extensions.noEffect

class OptionalCloseReducer<State, Action>(
    private val closeCallback: (State) -> State,
    private val shouldHandleAction: (Action) -> Boolean
) : Reducer<State, Action> {
    override fun reduce(state: MutableValue<State>, action: Action): List<Effect<Action>> {
        if (shouldHandleAction(action)) {
            state.mutate(closeCallback)
        }
        return noEffect()
    }
}

inline fun <State, Action, reified ActionToHandle> Reducer<State, Action>.handleClosableActionsUsing(
    noinline closeCallback: (State) -> State
): Reducer<State, Action> =
    combine(
        this,
        OptionalCloseReducer(closeCallback) { it.isOrWraps<ActionToHandle>() }
    )