package com.toggl.common.feature.navigation

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.combine
import com.toggl.architecture.core.isOrWraps
import com.toggl.architecture.extensions.noEffect

class OptionalCloseReducer<State : BackStackAwareState<State>, Action>(
    private val shouldHandleAction: (Action) -> Boolean
) : Reducer<State, Action> {
    override fun reduce(state: MutableValue<State>, action: Action): List<Effect<Action>> {

        if (shouldHandleAction(action)) {
            state.mutate { popBackStack() }
        }

        return noEffect()
    }
}

inline fun <State : BackStackAwareState<State>, Action, reified ActionToHandle>
    Reducer<State, Action>.handleClosableActionsUsing(): Reducer<State, Action> =
    combine(this, OptionalCloseReducer { it.isOrWraps<ActionToHandle>() })