package com.toggl.timer.start.domain

import com.toggl.architecture.core.Effect
import com.toggl.repository.timeentry.TimeEntryRepository

class StopTimeEntryEffect(private val repository: TimeEntryRepository) : Effect<StartTimeEntryAction> {
    override suspend fun execute(): StartTimeEntryAction? =
        repository.stopRunningTimeEntry()
            ?.run { StartTimeEntryAction.TimeEntryUpdated(id, this) }
}
