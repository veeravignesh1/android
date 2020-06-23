package com.toggl.calendar.contextualmenu.domain

import com.toggl.common.feature.domain.ProjectViewModel

sealed class ContextualMenuLabelsViewModel {
    abstract val description: String
    abstract val periodLabel: String

    data class TimeEntryContextualMenu(
        override val description: String,
        override val periodLabel: String,
        val projectViewModel: ProjectViewModel? = null
    ) : ContextualMenuLabelsViewModel()

    data class CalendarEventContextualMenu(
        override val description: String,
        override val periodLabel: String,
        val calendarColor: String?,
        val calendarName: String?
    ) : ContextualMenuLabelsViewModel()
}