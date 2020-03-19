package com.toggl.timer.common

import com.toggl.models.domain.TimeEntry
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

fun createTimeEntry(
    id: Long,
    description: String = "",
    startTime: OffsetDateTime = OffsetDateTime.now(),
    duration: Duration? = null
) =
    TimeEntry(
        id,
        description,
        startTime,
        duration,
        false,
        1,
        null,
        null,
        false
    )