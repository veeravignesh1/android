package com.toggl.api.clients.reports

import com.toggl.api.models.ReportsTotals
import java.time.OffsetDateTime

interface ReportsApiClient {
    suspend fun getTotals(userId: Long, workspaceId: Long, startDate: OffsetDateTime, endDate: OffsetDateTime): ReportsTotals
}
