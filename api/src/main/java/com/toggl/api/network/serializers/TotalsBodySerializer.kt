package com.toggl.api.network.serializers

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.toggl.api.network.models.reports.TotalsBody
import java.lang.reflect.Type
import java.time.format.DateTimeFormatter

internal class TotalsBodySerializer : JsonSerializer<TotalsBody> {

    override fun serialize(src: TotalsBody, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val reportsTotalsRequestJson = JsonObject()

        // Start Date
        reportsTotalsRequestJson.addProperty("start_date", src.startDate.format(DateTimeFormatter.ISO_DATE))

        // End Date, Optional
        if (src.endDate != null) {
            reportsTotalsRequestJson.addProperty("end_date", src.endDate.format(DateTimeFormatter.ISO_DATE))
        }

        // User Ids is a list, but the mobile app is single user
        val userIds = JsonArray()
        userIds.add(src.userId)
        reportsTotalsRequestJson.add("user_ids", userIds)

        // Always true
        reportsTotalsRequestJson.addProperty("with_graph", true)

        return reportsTotalsRequestJson
    }
}