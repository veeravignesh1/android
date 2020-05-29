package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.domain.CalendarAction

sealed class ContextualMenuAction {
    object CloseButtonTapped : ContextualMenuAction()
    object DiscardButtonTapped : ContextualMenuAction()
    object DialogDismissed : ContextualMenuAction()

    companion object {
        fun fromCalendarAction(calendarAction: CalendarAction): ContextualMenuAction? =
            if (calendarAction !is CalendarAction.ContextualMenu) null
            else calendarAction.contextualMenu

        fun toCalendarAction(contextualMenuAction: ContextualMenuAction): CalendarAction =
            CalendarAction.ContextualMenu(contextualMenuAction)
    }
}

fun ContextualMenuAction.formatForDebug() =
    when (this) {
        ContextualMenuAction.CloseButtonTapped -> "Contextual menu closed"
        ContextualMenuAction.DiscardButtonTapped -> "Contextual menu discarded"
        ContextualMenuAction.DialogDismissed -> "Contextual menu dialog dismissed"
    }