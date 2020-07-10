package com.toggl.calendar.datepicker.domain

import com.toggl.calendar.common.CoroutineTest
import com.toggl.common.extensions.toList
import com.toggl.models.domain.UserPreferences
import com.toggl.repository.interfaces.SettingsRepository
import io.kotlintest.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.DayOfWeek
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
@DisplayName("The LoadDatePickerDates effect")
class LoadDatePickerDatesEffectTest : CoroutineTest() {

    @ParameterizedTest
    @MethodSource("availableDatesTestDataForAnYear")
    fun `Loads correctly the available dates`(testData: AvailableDatesTestData) = runBlockingTest {
        val settingsRepository = mockk<SettingsRepository>()
        coEvery { settingsRepository.loadUserPreferences() }.returns(UserPreferences.default.copy(firstDayOfTheWeek = DayOfWeek.SUNDAY))
        val effect = LoadDatePickerDatesEffect(testData.today, settingsRepository, dispatcherProvider)

        val result = effect.execute()!!

        result.availableDates.last() shouldBe testData.today
        result.availableDates.first() shouldBe testData.lastAvailableDay
    }

    @ParameterizedTest
    @MethodSource("visibleDatesTestDataForTodayAsJuly_9_2020")
    fun `Loads correctly the visible dates for july, 9th 2020`(testData: VisibleDatesTestData) = runBlockingTest {
        val settingsRepository = mockk<SettingsRepository>()
        coEvery { settingsRepository.loadUserPreferences() }.returns(UserPreferences.default.copy(firstDayOfTheWeek = testData.beginningOfTheWeek))
        val effect = LoadDatePickerDatesEffect(testData.today, settingsRepository, dispatcherProvider)

        val result = effect.execute()!!

        result.visibleDates.last() shouldBe testData.lastVisibleDate
        result.visibleDates.first() shouldBe testData.firstVisibleDate
    }

    companion object {
        @JvmStatic
        fun availableDatesTestDataForAnYear(): List<AvailableDatesTestData> {
            val lastYear = OffsetDateTime.now().plusYears(-1)
            return (lastYear..OffsetDateTime.now())
                .toList()
                .map { AvailableDatesTestData(it, it.plusDays(-13)) }
        }

        @JvmStatic
        fun visibleDatesTestDataForTodayAsJuly_9_2020(): List<VisibleDatesTestData> {
            val today = OffsetDateTime.of(2020, Month.JULY.value, 9, 0, 0, 0, 0, ZoneOffset.UTC)
            val julyOf2020 = OffsetDateTime.of(2020, Month.JULY.value, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val juneOf2020 = OffsetDateTime.of(2020, Month.JUNE.value, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            return listOf(
                VisibleDatesTestData(today = today, beginningOfTheWeek = DayOfWeek.SUNDAY, firstVisibleDate = juneOf2020.withDayOfMonth(21), lastVisibleDate = julyOf2020.withDayOfMonth(11)),
                VisibleDatesTestData(today = today, beginningOfTheWeek = DayOfWeek.MONDAY, firstVisibleDate = juneOf2020.withDayOfMonth(22), lastVisibleDate = julyOf2020.withDayOfMonth(12)),
                VisibleDatesTestData(today = today, beginningOfTheWeek = DayOfWeek.TUESDAY, firstVisibleDate = juneOf2020.withDayOfMonth(23), lastVisibleDate = julyOf2020.withDayOfMonth(13)),
                VisibleDatesTestData(today = today, beginningOfTheWeek = DayOfWeek.WEDNESDAY, firstVisibleDate = juneOf2020.withDayOfMonth(24), lastVisibleDate = julyOf2020.withDayOfMonth(14)),
                VisibleDatesTestData(today = today, beginningOfTheWeek = DayOfWeek.THURSDAY, firstVisibleDate = juneOf2020.withDayOfMonth(25), lastVisibleDate = julyOf2020.withDayOfMonth(15)),
                VisibleDatesTestData(today = today, beginningOfTheWeek = DayOfWeek.FRIDAY, firstVisibleDate = juneOf2020.withDayOfMonth(26), lastVisibleDate = julyOf2020.withDayOfMonth(9)),
                VisibleDatesTestData(today = today, beginningOfTheWeek = DayOfWeek.SATURDAY, firstVisibleDate = juneOf2020.withDayOfMonth(20), lastVisibleDate = julyOf2020.withDayOfMonth(10))
            )
        }
    }

    data class AvailableDatesTestData(val today: OffsetDateTime, val lastAvailableDay: OffsetDateTime)
    data class VisibleDatesTestData(
        val today: OffsetDateTime,
        val beginningOfTheWeek: DayOfWeek,
        val firstVisibleDate: OffsetDateTime,
        val lastVisibleDate: OffsetDateTime
    )
}