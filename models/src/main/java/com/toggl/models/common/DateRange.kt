package com.toggl.models.common

import java.time.OffsetDateTime

data class DateRange(
    val start: OffsetDateTime,
    val end: OffsetDateTime?
)
