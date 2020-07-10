package com.toggl.common.extensions

import com.toggl.common.Constants.ClockMath.secondsInAMinute
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
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

fun OffsetDateTime.roundToClosestQuarter(): OffsetDateTime {
    val roundDown = this.roundDownToClosestQuarter()
    val roundUp = this.roundUpToClosestQuarter()

    val secondsToRoundDown = roundDown.roundDownToClosestQuarter().absoluteDurationBetween(this).seconds
    val secondsToRoundUp = roundUp.roundDownToClosestQuarter().absoluteDurationBetween(this).seconds
    return if (secondsToRoundDown > secondsToRoundUp) roundUp else roundDown
}

fun OffsetDateTime.roundUpToClosestQuarter(): OffsetDateTime {
    return if (this.minute >= 45) {
        val nextHour = this.plusHours(1)
        nextHour.truncatedTo(ChronoUnit.HOURS)
    } else {
        val offset = 15 - this.minute % 15
        val minute = this.minute + offset
        this.truncatedTo(ChronoUnit.HOURS).withMinute(minute)
    }
}

fun OffsetDateTime.roundDownToClosestQuarter(): OffsetDateTime {
    val offset = this.minute % 15
    val minute = this.minute - offset
    return this.truncatedTo(ChronoUnit.HOURS).withMinute(minute)
}

fun OffsetDateTime.maybePlus(duration: Duration?): OffsetDateTime? {
    if (duration == null) return null
    return this + duration
}

fun OffsetDateTime.toBeginningOfTheDay(): OffsetDateTime =
    this.with(LocalTime.MIN)

fun OffsetDateTime.toEndOfTheDay(): OffsetDateTime =
    this.with(LocalTime.MAX)

fun OffsetDateTime.beginningOfWeek(beginningOfWeek: DayOfWeek): OffsetDateTime {
    val offset = (7 + this.dayOfWeek.value - beginningOfWeek.value) % 7
    return this.plusDays(-offset.toLong())
}

fun ClosedRange<OffsetDateTime>.toList(step: TemporalUnit = ChronoUnit.DAYS): List<OffsetDateTime> {
    return sequence {
        var current = start
        while (current <= endInclusive) {
            yield(current)
            current = current.plus(1, step)
        }
    }.toList()
}