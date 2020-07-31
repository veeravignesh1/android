package com.toggl.reports.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportsReducer @Inject constructor() : Reducer<ReportsState, ReportsAction> {

    override fun reduce(
        state: MutableValue<ReportsState>,
        action: ReportsAction
    ): List<Effect<ReportsAction>> =
        when (action) {
            ReportsAction.ViewAppeared -> TODO()
        }
}
