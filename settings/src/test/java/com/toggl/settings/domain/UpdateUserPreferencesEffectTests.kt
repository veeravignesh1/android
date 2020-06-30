package com.toggl.settings.domain

import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SmartAlertsOption
import com.toggl.repository.interfaces.SettingsRepository
import com.toggl.settings.common.CoroutineTest
import com.toggl.settings.common.createUserPreferences
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.shouldBe
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
            isManualModeEnabled = true,
            is24HourClock = true,
            selectedWorkspaceId = 1,
            dateFormat = DateFormat.DDMMYYYY_dash,
            durationFormat = DurationFormat.Decimal,
            firstDayOfTheWeek = DayOfWeek.WEDNESDAY,
            shouldGroupSimilarTimeEntries = true,
            hasCellSwipeActions = true,
            smartAlertsOption = SmartAlertsOption.MinutesBefore15
        )
        val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
        val resultAction = UpdateUserPreferencesEffect(
            newUserPreferences,
            settingsRepository,
            dispatcherProvider
        ).execute()

        with(resultAction.userPreferences) {
            isManualModeEnabled.shouldBeTrue()
            is24HourClock.shouldBeTrue()
            selectedWorkspaceId shouldBe 1
            dateFormat shouldBe DateFormat.DDMMYYYY_dash
            durationFormat shouldBe DurationFormat.Decimal
            firstDayOfTheWeek shouldBe DayOfWeek.WEDNESDAY
            shouldGroupSimilarTimeEntries.shouldBeTrue()
            hasCellSwipeActions.shouldBeTrue()
            smartAlertsOption shouldBe SmartAlertsOption.MinutesBefore15
        }

        coVerify { settingsRepository.saveUserPreferences(newUserPreferences) }
    }
}