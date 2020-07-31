package com.toggl.api.network.models.reports

import com.toggl.api.models.Resolution

internal data class TotalsResponse(
    val seconds: Long,
    val graph: List<GraphItem>,
    val resolution: Resolution
)
