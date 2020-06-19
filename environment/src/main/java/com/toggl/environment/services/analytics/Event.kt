package com.toggl.environment.services.analytics

import com.toggl.environment.services.analytics.parameters.CalendarSuggestionProviderState
import com.toggl.environment.services.analytics.parameters.EditViewCloseReason
import com.toggl.environment.services.analytics.parameters.EditViewOpenReason
import com.toggl.environment.services.analytics.parameters.SuggestionProviderType
import com.toggl.environment.services.analytics.parameters.TimeEntryDeleteOrigin
import com.toggl.environment.services.analytics.parameters.TimeEntryStopOrigin
import java.time.Duration

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

        fun suggestionStarted(providerType: SuggestionProviderType) =
            Event("SuggestionStarted", mapOf("SuggestionProvider" to providerType.name))

        fun calendarSuggestionContinueEvent(duration: Duration): Event {
            val direction = if (duration.isNegative) "before" else "after"
            val text = when {
                duration < Duration.ofMinutes(5) -> "<5"
                duration < Duration.ofMinutes(15) -> "5-15"
                duration < Duration.ofMinutes(30) -> "15-30"
                duration < Duration.ofMinutes(60) -> "30-60"
                else -> ">60"
            }
            val offsetCategory = "$text $direction"
            return Event("CalendarSuggestionContinued", mapOf("Offset" to offsetCategory))
        }

        fun suggestionsPresented(
            suggestionsCount: Int,
            providerCounts: Map<String, String>,
            calendarProviderState: CalendarSuggestionProviderState,
            workspaceCount: Int
        ) = Event(
            "SuggestionsPresented", mapOf(
                "SuggestionsCount" to suggestionsCount.toString(),
                "CalendarProviderState" to calendarProviderState.name,
                "DistinctWorkspaceCount" to workspaceCount.toString()
            ) + providerCounts
        )
    }
}
