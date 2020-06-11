package com.toggl.common.extensions

import com.toggl.common.Constants.ClockMath.secondsInAMinute
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")

fun OffsetDateTime.minutesUntil(otherOffsetDateTime: OffsetDateTime) =
    this.until(otherOffsetDateTime, ChronoUnit.MINUTES)

fun OffsetDateTime.formatForDisplayingTime(): String = this.format(timeFormatter)
fun OffsetDateTime.formatForDisplayingDate(): String = this.format(dateFormatter)

fun OffsetDateTime.absoluteDurationBetween(other: OffsetDateTime): Duration =
    Duration.ofMillis(ChronoUnit.MILLIS.between(this, other).absoluteValue)

fun OffsetDateTime.timeOfDay(): Duration = Duration.ofNanos(toLocalTime().toNanoOfDay())

fun OffsetDateTime.roundToClosestMinute(): OffsetDateTime =
    if (this.second > secondsInAMinute / 2) {
        this + Duration.ofSeconds((secondsInAMinute - this.second).toLong())
    } else {
        this - Duration.ofSeconds(this.second.toLong())
    }

fun OffsetDateTime.maybePlus(duration: Duration?): OffsetDateTime? {
    if (duration == null) return null
    return this + duration
}