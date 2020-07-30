package com.toggl.timer.generators

import com.toggl.timer.common.createTimeEntry
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arb
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import java.time.ZoneOffset

val sameYearTimeEntryArb = arb { rs ->
    val ids = Arb.long().values(rs)
    val dates = Arb.localDateTime(2020, 2020).map { it.atOffset(ZoneOffset.UTC) }.values(rs)
    ids.zip(dates).map { createTimeEntry(it.first.value, "desc", it.second.value) }
}
