package com.toggl.api.models

import java.time.OffsetDateTime

data class ReportsTotals(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime?,
    val resolution: Resolution,
    val groups: List<ReportsTotalsGroup>
)
