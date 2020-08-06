package com.toggl.api.network.models.reports

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class TotalsBody(
    @Json(name = "start_date")
    val startDate: OffsetDateTime,
    @Json(name = "end_date")
    val endDate: OffsetDateTime?,
    @Json(name = "user_ids")
    val userIds: List<Long>,
    @Json(name = "with_graph")
    val withGraph: Boolean
)
