package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.TimeEntryRepository
import kotlinx.coroutines.withContext

class LoadTimeEntriesEffect(
    private val repository: TimeEntryRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<LoadingAction> {
    override suspend fun execute(): LoadingAction? =
        withContext(dispatcherProvider.computation) {
            LoadingAction.TimeEntriesLoaded(repository.loadTimeEntries())
        }
}
