package com.toggl.domain.reducers

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.domain.effect.LoadTimeEntriesEffect
import com.toggl.repository.timeentry.TimeEntryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntityLoadReducer @Inject constructor(
    private val repository: TimeEntryRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<AppState, AppAction> {

    override fun reduce(
        state: SettableValue<AppState>,
        action: AppAction
    ): List<Effect<AppAction>> =
        when (action) {
            AppAction.Load -> effect(LoadTimeEntriesEffect(repository, dispatcherProvider))
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