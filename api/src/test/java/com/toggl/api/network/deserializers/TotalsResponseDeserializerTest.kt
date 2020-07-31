package com.toggl.api.network.deserializers

import com.google.gson.JsonParser
import com.toggl.api.models.Resolution
import com.toggl.api.network.models.reports.GraphItem
import com.toggl.api.network.models.reports.TotalsResponse
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("The TotalsResponse deserializer")
class TotalsResponseDeserializerTest {
    @Test
    fun `properly deserializes the response from the totals endpoint`() {
        val json = JsonParser.parseString(validResponseJson).asJsonObject
        val jsonSerializer = TotalsResponseDeserializer()
        val user = jsonSerializer.deserialize(json, mockk(), mockk())

        user shouldBe TotalsResponse(
            seconds = 568,
            resolution = Resolution.Day,
            graph = listOf(
                GraphItem(12, mapOf("0" to 2108, "1" to 28561)),
                GraphItem(269, mapOf("0" to 548, "1" to 285))
            )
        )
    }

    companion object {
        private const val validResponseJson = "{\"seconds\":568,\"resolution\":\"day\",\"graph\":[{\"seconds\":12,\"by_rate\":{\"0\":2108,\"1\":28561}},{\"seconds\":269,\"by_rate\":{\"0\":548,\"1\":285}}]}"
    }
}
