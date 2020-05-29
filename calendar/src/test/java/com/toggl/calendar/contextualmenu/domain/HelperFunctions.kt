package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.models.domain.TimeEntry

fun createInitialState(
    timeEntries: List<TimeEntry> = emptyList(),
    selectedItem: SelectedCalendarItem? = null
) = ContextualMenuState(
    timeEntries.associateBy { it.id },
    selectedItem
)