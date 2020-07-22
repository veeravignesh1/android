package com.toggl.domain.reducers

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.extensions.noEffect
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.navigation.pop
import com.toggl.domain.AppAction
import com.toggl.domain.AppState

class BackStackReducer : AppReducer {
    override fun reduce(state: MutableValue<AppState>, action: AppAction): List<Effect<AppAction>> =
        when (action) {
            is AppAction.NavigateBack -> state.mutateWithoutEffects {
                when {
                    backStack.size > 1 -> copy(backStack = backStack.pop())
                    else -> this
                }
            }
            else -> noEffect()
        }

}