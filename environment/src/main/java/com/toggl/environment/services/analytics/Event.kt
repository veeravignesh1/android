package com.toggl.environment.services.analytics

import com.toggl.environment.services.analytics.parameters.EditViewCloseReason
import com.toggl.environment.services.analytics.parameters.EditViewOpenReason
import com.toggl.environment.services.analytics.parameters.TimeEntryDeleteOrigin
import com.toggl.environment.services.analytics.parameters.TimeEntryStopOrigin

class Event private constructor(val name: String, val parameters: Map<String, String>) {
    companion object {
        fun editViewClosed(reason: EditViewCloseReason) =
            Event("EditViewClosed", mapOf("Reason" to reason.name))

        fun editViewOpened(reason: EditViewOpenReason) =
            // The parameter is named "Origin" and not "Reason" for legacy reasons
            Event("EditViewOpened", mapOf("Origin" to reason.name))

        fun timeEntryStopped(origin: TimeEntryStopOrigin) =
            Event("TimeEntryStopped", mapOf("Origin" to origin.name))

        fun timeEntryDeleted(origin: TimeEntryDeleteOrigin) =
            // The parameter is named "Source" and not "Origin" for legacy reasons
            Event("DeleteTimeEntry", mapOf("Source" to origin.name))

        fun undoTapped() =
            Event("TimeEntryDeletionUndone", mapOf())
    }
}
