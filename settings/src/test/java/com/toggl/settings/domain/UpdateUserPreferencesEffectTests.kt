package com.toggl.settings.domain

import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
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
            hasCellSwipeActions = true
        )
        val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
        val resultAction = UpdateUserPreferencesEffect(
            newUserPreferences,
            settingsRepository,
            dispatcherProvider
        ).execute()

        resultAction.userPreferences.isManualModeEnabled.shouldBeTrue()
        resultAction.userPreferences.is24HourClock.shouldBeTrue()
        resultAction.userPreferences.selectedWorkspaceId shouldBe 1
        resultAction.userPreferences.dateFormat shouldBe DateFormat.DDMMYYYY_dash
        resultAction.userPreferences.durationFormat shouldBe DurationFormat.Decimal
        resultAction.userPreferences.firstDayOfTheWeek shouldBe DayOfWeek.WEDNESDAY
        resultAction.userPreferences.shouldGroupSimilarTimeEntries.shouldBeTrue()
        resultAction.userPreferences.hasCellSwipeActions.shouldBeTrue()

        coVerify { settingsRepository.saveUserPreferences(newUserPreferences) }
    }
}