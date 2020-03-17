package com.toggl.domain.effect

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.domain.AppAction
import com.toggl.repository.timeentry.TimeEntryRepository
import kotlinx.coroutines.withContext

class LoadTimeEntriesEffect(
    private val repository: TimeEntryRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<AppAction> {
    override suspend fun execute(): AppAction? =
        withContext(dispatcherProvider.computation) {
            AppAction.EntitiesLoaded(repository.loadTimeEntries())
        }
}
