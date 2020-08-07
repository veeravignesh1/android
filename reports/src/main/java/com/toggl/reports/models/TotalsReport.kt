package com.toggl.reports.models

import com.toggl.api.models.Resolution

data class TotalsReport(
    val resolution: Resolution,
    val groups: List<ReportsTotalsGroup>
)
