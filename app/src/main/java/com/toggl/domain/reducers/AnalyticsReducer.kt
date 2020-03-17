package com.toggl.domain.reducers

import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.noEffect
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.environment.services.analytics.AnalyticsService
import com.toggl.environment.services.analytics.Event

typealias AnalyticsReducer = Reducer<AppState, AppAction>

fun createAnalyticsReducer(analyticsService: AnalyticsService) =
    AnalyticsReducer { _, action ->
        action.toEvent()?.run { analyticsService.track(this) }
        noEffect()
    }

fun AppAction.toEvent(): Event? = null
