package com.toggl.common.feature

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.combine
import com.toggl.architecture.core.isOrWraps
import com.toggl.architecture.extensions.noEffect

class OptionalCloseReducer<State, Action, ActionToHandle>(
    private val closeCallback: (State) -> State,
    private val actionToHandle: Class<ActionToHandle>
) : Reducer<State, Action> {
    override fun reduce(state: MutableValue<State>, action: Action): List<Effect<Action>> {

        if (action.isOrWraps(actionToHandle)) {
            state.mutate(closeCallback)
        }

        return noEffect()
    }
}

fun <State, Action, ActionToHandle> Reducer<State, Action>.handleClosableActionsUsing(
    closeCallback: (State) -> State,
    actionToHandle: Class<ActionToHandle>
): Reducer<State, Action> =
    combine(this, OptionalCloseReducer(closeCallback, actionToHandle))