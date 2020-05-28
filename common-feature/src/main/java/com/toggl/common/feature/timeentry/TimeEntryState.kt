package com.toggl.common.feature.timeentry

import com.toggl.models.domain.TimeEntry

data class TimeEntryState(val timeEntries: Map<Long, TimeEntry>)
