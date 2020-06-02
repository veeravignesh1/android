package com.toggl.common.feature

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.noEffect

internal class OptionalCloseReducer<State, Action>(
    private val closeCallback: (State) -> State,
    private val actionIsCloseAction: (Action) -> Boolean
) : Reducer<State, Action> {
    override fun reduce(state: MutableValue<State>, action: Action): List<Effect<Action>> {
        if (actionIsCloseAction(action)) {
            state.mutate(closeCallback)
        }

        return noEffect()
    }
}

fun <State, Action> handleClosableActionsUsing(
    actionIsCloseAction: (Action) -> Boolean,
    closeCallback: (State) -> State
): Reducer<State, Action> = OptionalCloseReducer(closeCallback, actionIsCloseAction)