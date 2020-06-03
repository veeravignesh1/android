package com.toggl.calendar.contextualmenu.domain

import com.toggl.architecture.core.CloseAction
import com.toggl.calendar.common.domain.CalendarAction
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryActionHolder

sealed class ContextualMenuAction {
    object CloseButtonTapped : ContextualMenuAction(), CloseAction
    object DiscardButtonTapped : ContextualMenuAction(), CloseAction
    object DialogDismissed : ContextualMenuAction(), CloseAction
    object DeleteButtonTapped : ContextualMenuAction(), CloseAction
    object ContinueButtonTapped : ContextualMenuAction(), CloseAction
    object StopButtonTapped : ContextualMenuAction(), CloseAction
    object StartFromEventButtonTapped : ContextualMenuAction(), CloseAction
    object CopyAsTimeEntryButtonTapped : ContextualMenuAction(), CloseAction

    data class TimeEntryHandling(override val timeEntryAction: TimeEntryAction) : ContextualMenuAction(), TimeEntryActionHolder

    companion object {
        fun toCalendarAction(contextualMenuAction: ContextualMenuAction): CalendarAction =
            CalendarAction.ContextualMenu(contextualMenuAction)
    }
}

fun ContextualMenuAction.formatForDebug() =
    when (this) {
        ContextualMenuAction.CloseButtonTapped -> "Contextual menu closed"
        ContextualMenuAction.DiscardButtonTapped -> "Contextual menu discarded"
        ContextualMenuAction.DialogDismissed -> "Contextual menu dialog dismissed"
        ContextualMenuAction.DeleteButtonTapped -> "Delete button tapped"
        ContextualMenuAction.ContinueButtonTapped -> "Continue button tapped"
        ContextualMenuAction.StopButtonTapped -> "Stop button tapped"
        ContextualMenuAction.StartFromEventButtonTapped -> "Start button tapped"
        ContextualMenuAction.CopyAsTimeEntryButtonTapped -> "Copy button tapped"
        is ContextualMenuAction.TimeEntryHandling -> "Time entry action $timeEntryAction"
    }