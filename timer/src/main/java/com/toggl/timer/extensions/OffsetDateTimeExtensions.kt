package com.toggl.timer.extensions

import com.toggl.timer.startedit.util.MathConstants
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import kotlin.math.absoluteValue

val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")

fun OffsetDateTime.formatForDisplayingTime() = this.format(timeFormatter)
fun OffsetDateTime.formatForDisplayingDate() = this.format(dateFormatter)

fun OffsetDateTime.absoluteDurationBetween(other: OffsetDateTime): Duration =
    Duration.ofMillis(ChronoUnit.MILLIS.between(this, other).absoluteValue)

fun OffsetDateTime.timeOfDay(): Duration = Duration.ofNanos(toLocalTime().toNanoOfDay())

fun OffsetDateTime.roundToClosestMinute(): OffsetDateTime =
    if (this.second > MathConstants.secondsInAMinute / 2) {
        this + Duration.ofSeconds((MathConstants.secondsInAMinute - this.second).toLong())
    } else {
        this - Duration.ofSeconds(this.second.toLong())
    }
