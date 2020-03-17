package com.toggl.timer.common.domain

import com.toggl.architecture.core.Effect
import com.toggl.models.domain.TimeEntry
import com.toggl.repository.timeentry.TimeEntryRepository

class DeleteTimeEntriesEffect<Action>(
    private val repository: TimeEntryRepository,
    private val timeEntriesToDelete: List<TimeEntry>,
    private val mapFn: (HashSet<TimeEntry>) -> Action
) : Effect<Action> {
    override suspend fun execute(): Action? =
        repository
            .deleteTimeEntries(timeEntriesToDelete)
            .run(mapFn)
}
