package com.toggl.onboarding.login.domain

import com.toggl.api.login.LoginApiClient
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import com.toggl.onboarding.common.validPassword
import com.toggl.onboarding.domain.actions.OnboardingAction
import com.toggl.onboarding.domain.reducers.OnboardingReducer
import com.toggl.onboarding.domain.states.password
import com.toggl.repository.interfaces.UserRepository
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The password entered action")
class PasswordEnteredActionTests : CoroutineTest() {
    private val loginApi: LoginApiClient = mockk()
    private val userRepository: UserRepository = mockk()
    private val reducer = OnboardingReducer(loginApi, userRepository, dispatcherProvider)

    @Test
    fun `sets the password`() = runBlockingTest {
        val initialState = emptyState()

        reducer.testReduceState(initialState, OnboardingAction.PasswordEntered(validPassword.toString())) { newState ->
            newState shouldBe initialState.copy(localState = initialState.localState.copy(password = validPassword))
        }
    }
}