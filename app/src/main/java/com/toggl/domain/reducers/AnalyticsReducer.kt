package com.toggl.domain.reducers

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.noEffect
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.environment.services.analytics.AnalyticsService
import com.toggl.environment.services.analytics.Event
import javax.inject.Inject

class AnalyticsReducer @Inject constructor(
    private val analyticsService: AnalyticsService
) : Reducer<AppState, AppAction> {
    override fun reduce(
        state: SettableValue<AppState>,
        action: AppAction
    ): List<Effect<AppAction>> {
        action.toEvent()?.run { analyticsService.track(this) }
        return noEffect()
    }
}

fun AppAction.toEvent(): Event? = null
