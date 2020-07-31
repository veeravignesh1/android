package com.toggl.api.network.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.toggl.api.network.models.reports.GraphItem
import com.toggl.api.models.Resolution
import com.toggl.api.network.models.reports.TotalsResponse
import java.lang.reflect.Type

internal class TotalsResponseDeserializer : JsonDeserializer<TotalsResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TotalsResponse {
        val jsonObject = json?.asJsonObject ?: throw IllegalStateException()

        return TotalsResponse(
            seconds = jsonObject["seconds"].asLong,
            resolution = jsonObject["resolution"].asResolution,
            graph = jsonObject["graph"].asJsonArray.map { graphItemJson ->

                val graphJsonObject = graphItemJson.asJsonObject
                val seconds = graphJsonObject["seconds"].asLong
                val byRateJsonMap = graphJsonObject["by_rate"].asJsonObject
                val byRate = byRateJsonMap.keySet().map { it to byRateJsonMap[it].asLong }.toMap()

                GraphItem(seconds, byRate)
            }
        )
    }

    private val JsonElement.asResolution: Resolution
        get() = Resolution.valueOf(asString.capitalize())
}