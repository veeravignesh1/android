package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.domain.SelectedCalendarItem

fun createInitialState(
    selectedItem: SelectedCalendarItem? = null
) = ContextualMenuState(selectedItem)