package com.toggl.calendar.contextualmenu.domain

import com.toggl.common.feature.domain.ProjectViewModel

sealed class ContextualMenuViewModel {
    abstract val description: String
    abstract val periodLabel: String
    abstract val contextualMenuActions: ContextualMenuActionsViewModel

    data class TimeEntryContextualMenu(
        override val description: String,
        override val periodLabel: String,
        val projectViewModel: ProjectViewModel? = null,
        override val contextualMenuActions: ContextualMenuActionsViewModel
    ) : ContextualMenuViewModel()

    data class CalendarEventContextualMenu(
        override val description: String,
        override val periodLabel: String,
        val calendarColor: String?,
        val calendarName: String?
    ) : ContextualMenuViewModel() {
        override val contextualMenuActions = ContextualMenuActionsViewModel.CalendarEventActions
    }
}
