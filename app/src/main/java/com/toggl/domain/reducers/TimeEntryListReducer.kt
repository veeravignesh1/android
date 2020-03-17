package com.toggl.domain.reducers

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.noEffect
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.domain.effect.LoadTimeEntriesEffect
import com.toggl.repository.timeentry.TimeEntryRepository

typealias TimeEntryListReducer = Reducer<AppState, AppAction>

fun createTimeEntryListReducer(repository: TimeEntryRepository, dispatcherProvider: DispatcherProvider) =
    Reducer<AppState, AppAction> { state, action ->
        when (action) {
            AppAction.Load -> LoadTimeEntriesEffect(repository, dispatcherProvider)
            is AppAction.EntitiesLoaded -> {
                state.value = state.value.copy(
                    timeEntries = action.timeEntries.associateBy { it.id }
                )
                noEffect()
            }
            is AppAction.Onboarding,
            is AppAction.Timer -> noEffect()
        }
    }
