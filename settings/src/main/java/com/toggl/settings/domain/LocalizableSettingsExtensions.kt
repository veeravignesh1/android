package com.toggl.settings.domain

import android.content.Context
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SmartAlertsOption
import com.toggl.settings.R
import java.time.DayOfWeek

private val daysOfWeekStringResources = mapOf(
    DayOfWeek.MONDAY to R.string.monday,
    DayOfWeek.TUESDAY to R.string.tuesday,
    DayOfWeek.WEDNESDAY to R.string.wednesday,
    DayOfWeek.THURSDAY to R.string.thursday,
    DayOfWeek.FRIDAY to R.string.friday,
    DayOfWeek.SATURDAY to R.string.saturday,
    DayOfWeek.SUNDAY to R.string.sunday
)

private val durationFormatStringResources = mapOf(
    DurationFormat.Classic to R.string.duration_format_classic,
    DurationFormat.Improved to R.string.duration_format_improved,
    DurationFormat.Decimal to R.string.duration_format_decimal
)

private val smartAlertsOptionsStringResources = mapOf(
    SmartAlertsOption.Disabled to R.string.disabled,
    SmartAlertsOption.WhenEventStarts to R.string.when_event_starts,
    SmartAlertsOption.MinutesBefore5 to R.string.five_minutes_before,
    SmartAlertsOption.MinutesBefore10 to R.string.ten_minutes_before,
    SmartAlertsOption.MinutesBefore15 to R.string.fifteen_minutes_before,
    SmartAlertsOption.MinutesBefore30 to R.string.thirty_minutes_before,
    SmartAlertsOption.MinutesBefore60 to R.string.one_hour_before
)

fun DurationFormat.getTranslatedRepresentation(context: Context) =
    context.getString(durationFormatStringResources[this] ?: R.string.duration_format_improved)

fun DayOfWeek.getTranslatedRepresentation(context: Context) =
    context.getString(daysOfWeekStringResources[this] ?: R.string.monday)

fun SmartAlertsOption.getTranslatedRepresentation(context: Context) =
    context.getString(smartAlertsOptionsStringResources[this] ?: R.string.disabled)