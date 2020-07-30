package com.toggl.api.network.models.reports

import java.time.OffsetDateTime

internal data class TotalsBody(
    val userId: Long,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime?
)