package com.toggl.onboarding.login.domain

import com.toggl.api.login.LoginApiClient
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import com.toggl.onboarding.common.validEmail
import com.toggl.onboarding.domain.actions.OnboardingAction
import com.toggl.onboarding.domain.reducers.OnboardingReducer
import com.toggl.onboarding.domain.states.email
import com.toggl.repository.interfaces.UserRepository
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The email entered action")
class EmailEnteredActionTests : CoroutineTest() {
    private val loginApi: LoginApiClient = mockk()
    private val userRepository: UserRepository = mockk()
    private val reducer = OnboardingReducer(loginApi, userRepository, dispatcherProvider)

    @Test
    fun `sets the email in the state`() = runBlocking {
        reducer.testReduceState(emptyState(), OnboardingAction.EmailEntered(validEmail.toString())) { newState ->
            newState.email.email shouldBe validEmail.email
        }
    }
}
