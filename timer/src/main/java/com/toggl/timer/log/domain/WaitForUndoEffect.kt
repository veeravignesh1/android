package com.toggl.timer.log.domain

import com.toggl.architecture.core.Effect
import com.toggl.common.Constants.timeEntryDeletionDelayMs
import kotlinx.coroutines.delay

class WaitForUndoEffect(
    private val timeEntryIdsToDelete: List<Long>
) : Effect<TimeEntriesLogAction> {

    override suspend fun execute(): TimeEntriesLogAction? {
        delay(timeEntryDeletionDelayMs)

        return TimeEntriesLogAction.CommitDeletion(timeEntryIdsToDelete)
    }
}
