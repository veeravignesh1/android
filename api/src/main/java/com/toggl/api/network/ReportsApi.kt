package com.toggl.api.network

import com.toggl.api.network.models.reports.TotalsBody
import com.toggl.api.network.models.reports.TotalsResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface ReportsApi {
    @POST("workspace/{workspaceId}/search/date_entries/totals")
    suspend fun totals(@Path("workspaceId") workspaceId: Long, @Body totalsBody: TotalsBody): TotalsResponse
}
