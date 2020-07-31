package com.toggl.api.network.serializers

import com.toggl.api.network.models.reports.TotalsBody
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

@DisplayName("The TotalsBody serializer")
class TotalsBodySerializerTest {
    @Test
    fun `turns the TotalsBody object into the json the api expects`() {

        val expectedJson = "{\"start_date\":\"2020-06-29Z\",\"end_date\":\"2020-07-05Z\",\"user_ids\":[4674715],\"with_graph\":true}"
        val totalsBody = TotalsBody(
            userId = 4674715,
            startDate = OffsetDateTime.of(2020, 6, 29, 0, 0, 0, 0, ZoneOffset.UTC),
            endDate = OffsetDateTime.of(2020, 7, 5, 3, 0, 0, 0, ZoneOffset.UTC)
        )
        val jsonSerializer = TotalsBodySerializer()

        val json = jsonSerializer.serialize(totalsBody, mockk(), mockk())

        json.toString() shouldBe expectedJson
    }
}
