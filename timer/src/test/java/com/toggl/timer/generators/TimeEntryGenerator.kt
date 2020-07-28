package com.toggl.timer.generators

import com.toggl.models.domain.TimeEntry
import com.toggl.timer.common.createTimeEntry

import java.time.Duration
import java.time.Month
import java.time.OffsetDateTime
import java.time.Year
import java.time.ZoneOffset
import kotlin.random.Random

fun randomTimeEntries(
    description: String? = null,
    duration: Duration? = null,
    year: Year? = null,
    month: Month? = null,
    day: Int? = null,
    hour: Int? = null,
    minute: Int? = null,
    second: Int? = null
): Sequence<TimeEntry> {
    return generateSequence {
        createTimeEntry(
            id = Random.nextLong(1, Long.MAX_VALUE),
            description = description ?: Random.nextLong().toString(),
            startTime = randomDateTime(year, month, day, hour, minute, second),
            duration = duration ?: randomDuration()
        )
    }
}

fun fixedTimeEntries() = (1L..100L)
    .map { index ->
        createTimeEntry(
            id = index,
            description = "Test $index",
            startTime = OffsetDateTime.now().minusDays(index),
            duration = Duration.ofHours(index)
        )
    }

fun randomDateTime(
    year: Year? = null,
    month: Month? = null,
    day: Int? = null,
    hour: Int? = null,
    minute: Int? = null,
    second: Int? = null
): OffsetDateTime {
    val y = year?.value ?: Random.nextInt(Year.MIN_VALUE, Year.MAX_VALUE)
    val m = month?.value ?: Random.nextInt(Month.JANUARY.value, Month.DECEMBER.value)
    val d = day ?: Random.nextInt(1, 28)
    val h = hour ?: Random.nextInt(1, 24)
    val min = minute ?: Random.nextInt(0, 60)
    val s = second ?: Random.nextInt(0, 60)
    return OffsetDateTime.of(y, m, d, h, min, s, 1, ZoneOffset.UTC)
}

fun randomDuration(): Duration {
    return Random.nextLong(1, 999).let { Duration.ofHours(it) }
}