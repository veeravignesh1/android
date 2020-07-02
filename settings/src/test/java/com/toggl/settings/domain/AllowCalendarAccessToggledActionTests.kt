package com.toggl.settings.domain

import com.toggl.environment.services.permissions.PermissionCheckerService
import com.toggl.repository.interfaces.SettingsRepository
import com.toggl.settings.common.CoroutineTest
import com.toggl.settings.common.createSettingsReducer
import com.toggl.settings.common.createSettingsState
import com.toggl.settings.common.testReduce
import io.kotlintest.matchers.collections.shouldBeSingleton
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The AllowCalendarAccessToggled action")
class AllowCalendarAccessToggledActionTests : CoroutineTest() {
    private val initialState = createSettingsState()
    private val permissionChecker: PermissionCheckerService = mockk(relaxed = true)
    private val settingsRepository: SettingsRepository = mockk(relaxed = true)
    private val reducer =
        createSettingsReducer(
            permissionCheckerService = permissionChecker,
            settingsRepository = settingsRepository,
            dispatcherProvider = dispatcherProvider
        )

    @Nested
    @DisplayName("When the calendar permission has been granted")
    inner class WhenTheCalendarPermissionHasAlreadyBeenGranted {

        init {
            every { permissionChecker.hasCalendarPermission() } returns true
        }

        @Test
        fun `Should just emit the UpdateUserPreferencesEffect when switching on`() = runBlockingTest {
            reducer.testReduce(
                initialState,
                SettingsAction.AllowCalendarAccessToggled(isCalendarAccessEnabled = true)
            ) { state, effects ->
                effects.shouldBeSingleton()
                state shouldBe initialState
                effects.first().shouldBeInstanceOf<UpdateUserPreferencesEffect>()
            }
        }

        @Test
        fun `Should emit the UpdateUserPreferencesEffect when switching off and clear selected calendar ids`() =
            runBlockingTest {
                reducer.testReduce(
                    initialState,
                    SettingsAction.AllowCalendarAccessToggled(isCalendarAccessEnabled = false)
                ) { state, effects ->
                    effects.shouldBeSingleton()
                    state shouldBe initialState
                    effects.first().shouldBeInstanceOf<UpdateUserPreferencesEffect>()

                    effects.first().execute()
                    coVerify(exactly = 1) {
                        settingsRepository.saveUserPreferences(
                            initialState.userPreferences.copy(
                                isCalendarIntegrationEnabled = false,
                                calendarIds = emptyList()
                            )
                        )
                    }
                }
            }
    }

    @Nested
    @DisplayName("When the calendar permission has not been granted already")
    inner class WhenTheCalendarPermissionHasNotBeenGranted {
        init {
            every { permissionChecker.hasCalendarPermission() } returns false
        }

        @Test
        fun `Should just emit the UpdateUserPreferencesEffect when switching off`() = runBlockingTest {
            reducer.testReduce(
                initialState,
                SettingsAction.AllowCalendarAccessToggled(isCalendarAccessEnabled = false)
            ) { state, effects ->
                effects.shouldBeSingleton()
                state shouldBe initialState
                effects.first().shouldBeInstanceOf<UpdateUserPreferencesEffect>()
            }
        }

        @Test
        fun `Should just emit the UpdateUserPreferencesEffect and the RequestCalendarPermissionEffect when switching on`() = runBlockingTest {
            reducer.testReduce(
                initialState,
                SettingsAction.AllowCalendarAccessToggled(isCalendarAccessEnabled = true)
            ) { state, effects ->
                effects.size shouldBe 2
                state shouldBe initialState
                effects.first().shouldBeInstanceOf<UpdateUserPreferencesEffect>()
                effects.last().shouldBeInstanceOf<RequestCalendarPermissionEffect>()
            }
        }
    }
}