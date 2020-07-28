package com.toggl.common

import com.google.common.truth.Truth.assertThat
import com.toggl.common.extensions.absoluteDurationBetween
import com.toggl.common.extensions.beginningOfWeek
import com.toggl.common.extensions.roundToClosestMinute
import com.toggl.common.extensions.timeOfDay
import com.toggl.common.extensions.toList

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.DayOfWeek
import java.time.Duration
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.stream.Stream

internal class OffsetDateTimeExtensionsKtTest {

    data class DurationTestEntry(val start: OffsetDateTime, val end: OffsetDateTime, val expectedDuration: Duration)

    @ParameterizedTest
    @MethodSource("absoluteDurationBetweenTheory")
    fun absoluteDurationBetween(durationTestEntry: DurationTestEntry) {
        val result = durationTestEntry.start.absoluteDurationBetween(durationTestEntry.end)

        assertThat(result).isEqualTo(durationTestEntry.expectedDuration)
    }

    @ParameterizedTest
    @MethodSource("timeOfDayTheory")
    fun timeOfDay(timeOfDayDurationPair: Pair<OffsetDateTime, Duration>) {
        val timeOfDay = timeOfDayDurationPair.first.timeOfDay()

        assertThat(timeOfDay).isAtMost(Duration.ofHours(24))
        assertThat(timeOfDay).isAtLeast((Duration.ofHours(0)))
        assertThat(timeOfDay).isEqualTo(timeOfDayDurationPair.second)
    }

    @Test
    fun roundToClosestMinuteRoundsToTheClosestMinute() {
        val tenOClock = OffsetDateTime.of(2020, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC)
        val onePastTen = tenOClock.plusMinutes(1)

        assertThat(tenOClock.roundToClosestMinute()).isEqualTo(tenOClock)
        assertThat((tenOClock + Duration.ofSeconds(1)).roundToClosestMinute()).isEqualTo(tenOClock)
        assertThat((tenOClock + Duration.ofSeconds(29)).roundToClosestMinute()).isEqualTo(tenOClock)
        assertThat((tenOClock + Duration.ofSeconds(30)).roundToClosestMinute()).isEqualTo(tenOClock)
        assertThat((tenOClock + Duration.ofSeconds(31)).roundToClosestMinute()).isEqualTo(onePastTen)
        assertThat((tenOClock + Duration.ofSeconds(59)).roundToClosestMinute()).isEqualTo(onePastTen)
        assertThat(onePastTen.roundToClosestMinute()).isEqualTo(onePastTen)
    }

    @Test
    fun beginningOfTheWeek() {
        val date = OffsetDateTime.of(2020, Month.JULY.value, 9, 0, 0, 0, 0, ZoneOffset.UTC)
        assertThat(date.dayOfWeek).isEqualTo(DayOfWeek.THURSDAY)

        assertThat(date.beginningOfWeek(DayOfWeek.SUNDAY)).isEqualTo(OffsetDateTime.of(2020, Month.JULY.value, 5, 0, 0, 0, 0, ZoneOffset.UTC))
        assertThat(date.beginningOfWeek(DayOfWeek.MONDAY)).isEqualTo(OffsetDateTime.of(2020, Month.JULY.value, 6, 0, 0, 0, 0, ZoneOffset.UTC))
        assertThat(date.beginningOfWeek(DayOfWeek.TUESDAY)).isEqualTo(OffsetDateTime.of(2020, Month.JULY.value, 7, 0, 0, 0, 0, ZoneOffset.UTC))
        assertThat(date.beginningOfWeek(DayOfWeek.WEDNESDAY)).isEqualTo(OffsetDateTime.of(2020, Month.JULY.value, 8, 0, 0, 0, 0, ZoneOffset.UTC))
        assertThat(date.beginningOfWeek(DayOfWeek.THURSDAY)).isEqualTo(date)
        assertThat(date.beginningOfWeek(DayOfWeek.FRIDAY)).isEqualTo(OffsetDateTime.of(2020, Month.JULY.value, 3, 0, 0, 0, 0, ZoneOffset.UTC))
        assertThat(date.beginningOfWeek(DayOfWeek.SATURDAY)).isEqualTo(OffsetDateTime.of(2020, Month.JULY.value, 4, 0, 0, 0, 0, ZoneOffset.UTC))
    }

    @Test
    fun closedRangeToList() {
        val start = OffsetDateTime.of(2020, Month.JULY.value, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        val end = OffsetDateTime.of(2020, Month.JULY.value, 7, 0, 0, 0, 0, ZoneOffset.UTC)

        assertThat((start..end).toList().size).isEqualTo(7)
        assertThat((end..start).toList()).isEmpty()
    }

    companion object {

        @JvmStatic
        fun absoluteDurationBetweenTheory(): List<DurationTestEntry> {
            val now = OffsetDateTime.now()
            return listOf(
                DurationTestEntry(
                    now,
                    now.plusMinutes(30),
                    Duration.ofMinutes(30)
                ),
                DurationTestEntry(
                    now.plusMinutes(
                        30
                    ), now, Duration.ofMinutes(30)
                ),

                DurationTestEntry(
                    now.plusSeconds(
                        59
                    ), now, Duration.ofSeconds(59)
                ),
                DurationTestEntry(
                    now,
                    now.plusSeconds(59),
                    Duration.ofSeconds(59)
                ),

                DurationTestEntry(
                    now.plusHours(123).plusMinutes(4).plusSeconds(5),
                    now,
                    Duration.ofHours(123).plusMinutes(4).plusSeconds(5)
                ),
                DurationTestEntry(
                    now,
                    now.plusHours(123).plusMinutes(4).plusSeconds(5),
                    Duration.ofHours(123).plusMinutes(4).plusSeconds(5)
                )
            )
        }

        @JvmStatic
        fun timeOfDayTheory(): Stream<Pair<OffsetDateTime, Duration>> {
            val now = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS)
            val inTwoDays = now.plusDays(2)
            var increment = Duration.ofMinutes(10).plusSeconds(1)

            val stream = Stream.builder<Pair<OffsetDateTime, Duration>>()
            stream.add(now to Duration.ZERO)
            while ((now + increment).isBefore(inTwoDays)) {
                stream.add(now + increment to increment.truncateDay())
                increment += Duration.ofMinutes(10)
            }
            stream.add(
                inTwoDays - Duration.ofSeconds(1) to Duration.ofHours(23) + Duration.ofMinutes(59) + Duration.ofSeconds(
                    59
                )
            )
            stream.add(inTwoDays to Duration.ZERO)
            return stream.build()
        }

        private fun Duration.truncateDay(): Duration =
            if (this < Duration.ofHours(24)) this
            else this - Duration.ofHours(24)
    }
}