package com.toggl.domain.reducers

import android.util.Log
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.noEffect
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.onboarding.domain.actions.formatForDebug
import com.toggl.timer.common.domain.formatForDebug
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingReducer @Inject constructor() : Reducer<AppState, AppAction> {
    override fun reduce(
        state: SettableValue<AppState>,
        action: AppAction
    ): List<Effect<AppAction>> {
        Log.i(
            "LoggingReducer", when (action) {
                is AppAction.Onboarding -> action.onboarding.formatForDebug()
                is AppAction.Timer -> action.timer.formatForDebug()
                AppAction.Load -> "Initial load of entities"
                is AppAction.EntitiesLoaded -> "Entities loaded"
            }
        )

        return noEffect()
    }
}