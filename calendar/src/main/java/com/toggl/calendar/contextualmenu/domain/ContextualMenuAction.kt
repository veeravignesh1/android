package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.domain.CalendarAction
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryActionHolder

sealed class ContextualMenuAction {
    object CloseButtonTapped : ContextualMenuAction()
    object DiscardButtonTapped : ContextualMenuAction()
    object DialogDismissed : ContextualMenuAction()
    object DeleteButtonTapped : ContextualMenuAction()
    object ContinueButtonTapped : ContextualMenuAction()
    object StopButtonTapped : ContextualMenuAction()
    object StartFromEventButtonTapped : ContextualMenuAction()
    object CopyAsTimeEntryButtonTapped : ContextualMenuAction()

    data class TimeEntryHandling(override val timeEntryAction: TimeEntryAction) : ContextualMenuAction(), TimeEntryActionHolder

    companion object {
        fun fromCalendarAction(calendarAction: CalendarAction): ContextualMenuAction? =
            if (calendarAction !is CalendarAction.ContextualMenu) null
            else calendarAction.contextualMenu

        fun toCalendarAction(contextualMenuAction: ContextualMenuAction): CalendarAction =
            CalendarAction.ContextualMenu(contextualMenuAction)
    }
}

fun ContextualMenuAction.isCloseAction() = when (this) {
    ContextualMenuAction.CloseButtonTapped,
    ContextualMenuAction.DiscardButtonTapped,
    ContextualMenuAction.DialogDismissed,
    ContextualMenuAction.DeleteButtonTapped,
    ContextualMenuAction.ContinueButtonTapped,
    ContextualMenuAction.StopButtonTapped,
    ContextualMenuAction.StartFromEventButtonTapped,
    ContextualMenuAction.CopyAsTimeEntryButtonTapped -> true
    is ContextualMenuAction.TimeEntryHandling -> false
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