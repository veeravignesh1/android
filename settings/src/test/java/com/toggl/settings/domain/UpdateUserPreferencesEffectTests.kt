package com.toggl.settings.domain

import com.toggl.models.domain.UserPreferences
import com.toggl.repository.interfaces.SettingsRepository
import com.toggl.settings.common.CoroutineTest
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The UpdateUserPreferences effect")
class UpdateUserPreferencesEffectTests : CoroutineTest() {

    @Test
    fun `Should save and return new user preferences`() = runBlockingTest {
        val newUserPreferences = UserPreferences(isManualModeEnabled = true)
        val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
        val resultAction = UpdateUserPreferencesEffect(
            newUserPreferences,
            settingsRepository,
            dispatcherProvider
        ).execute()

        resultAction.userPreferences.isManualModeEnabled.shouldBeTrue()
        coVerify { settingsRepository.saveUserPreferences(newUserPreferences) }
    }
}