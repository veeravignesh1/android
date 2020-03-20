package com.toggl.domain.reducers

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.HigherOrderReducer
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.domain.AppAction
import com.toggl.domain.AppState

class FeatureAvailabilityReducer(override val innerReducer: Reducer<AppState, AppAction>)
    : HigherOrderReducer<AppState, AppAction> {
    override fun reduce(
        state: SettableValue<AppState>,
        action: AppAction
    ): List<Effect<AppAction>> =
        innerReducer.reduce(state, action)
}