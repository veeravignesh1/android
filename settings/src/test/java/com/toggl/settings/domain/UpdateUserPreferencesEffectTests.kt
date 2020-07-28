package com.toggl.settings.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SmartAlertsOption
import com.toggl.repository.interfaces.SettingsRepository
import com.toggl.settings.common.CoroutineTest
import com.toggl.settings.common.createUserPreferences

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.DayOfWeek

@ExperimentalCoroutinesApi
@DisplayName("The UpdateUserPreferences effect")
class UpdateUserPreferencesEffectTests : CoroutineTest() {

    @Test
    fun `Should save and return new user preferences`() = runBlockingTest {
        val newUserPreferences = createUserPreferences(
            manualModeEnabled = true,
            twentyFourHourClockEnabled = true,
            groupSimilarTimeEntriesEnabled = true,
            cellSwipeActionsEnabled = true,
            calendarIntegrationEnabled = true,
            calendarIds = listOf("one", "two"),
            selectedWorkspaceId = 1,
            dateFormat = DateFormat.DDMMYYYY_dash,
            durationFormat = DurationFormat.Decimal,
            firstDayOfTheWeek = DayOfWeek.WEDNESDAY,
            smartAlertsOption = SmartAlertsOption.MinutesBefore15
        )
        val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
        val resultAction = UpdateUserPreferencesEffect(
            newUserPreferences,
            settingsRepository,
            dispatcherProvider
        ).execute()

        with(resultAction.userPreferences) {
            assertThat(manualModeEnabled).isTrue()
            assertThat(twentyFourHourClockEnabled).isTrue()
            assertThat(groupSimilarTimeEntriesEnabled).isTrue()
            assertThat(cellSwipeActionsEnabled).isTrue()
            assertThat(calendarIntegrationEnabled).isTrue()
            assertThat(calendarIds).isEqualTo(listOf("one", "two"))
            assertThat(selectedWorkspaceId).isEqualTo(1)
            assertThat(dateFormat).isEqualTo(DateFormat.DDMMYYYY_dash)
            assertThat(durationFormat).isEqualTo(DurationFormat.Decimal)
            assertThat(firstDayOfTheWeek).isEqualTo(DayOfWeek.WEDNESDAY)
            assertThat(smartAlertsOption).isEqualTo(SmartAlertsOption.MinutesBefore15)
        }

        coVerify { settingsRepository.saveUserPreferences(newUserPreferences) }
    }
}