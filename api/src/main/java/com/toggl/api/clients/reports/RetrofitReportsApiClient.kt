package com.toggl.api.clients.reports

import com.toggl.api.exceptions.ReportsRangeTooLongException
import com.toggl.api.models.ReportsTotals
import com.toggl.api.models.ReportsTotalsGroup
import com.toggl.api.network.ReportsApi
import com.toggl.api.network.models.reports.TotalsBody
import com.toggl.common.Constants
import java.time.Duration
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitReportsApiClient @Inject constructor(
    private val reportsApi: ReportsApi
) : ReportsApiClient {

    override suspend fun getTotals(userId: Long, workspaceId: Long, startDate: OffsetDateTime, endDate: OffsetDateTime): ReportsTotals {
        if (endDate.minusDays(Constants.Reports.maximumRangeInDays) > startDate)
            throw ReportsRangeTooLongException()

        val body = TotalsBody(userId, startDate, endDate)
        val response = reportsApi.totals(workspaceId, body)

        return ReportsTotals(
            startDate = startDate,
            endDate = endDate,
            resolution = response.resolution,
            groups = response.graph.map { group ->
                ReportsTotalsGroup(
                    total = Duration.ofSeconds(group.seconds),
                    billable = Duration.ofSeconds(group.byRate.values.sum())
                )
            }
        )
    }
}