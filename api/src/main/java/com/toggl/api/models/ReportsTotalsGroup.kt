package com.toggl.api.models

import java.time.Duration

data class ReportsTotalsGroup(
    val total: Duration,
    val billable: Duration
)