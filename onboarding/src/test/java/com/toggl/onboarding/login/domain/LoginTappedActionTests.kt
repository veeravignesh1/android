package com.toggl.onboarding.login.domain

import com.toggl.api.login.LoginApiClient
import com.toggl.architecture.Loadable
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import com.toggl.onboarding.common.validEmail
import com.toggl.onboarding.common.validPassword
import com.toggl.onboarding.domain.actions.OnboardingAction
import com.toggl.onboarding.domain.reducers.OnboardingReducer
import com.toggl.onboarding.domain.states.OnboardingState
import com.toggl.repository.interfaces.UserRepository
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The login tapped action")
class LoginTappedActionTests : CoroutineTest() {
    private val loginApi: LoginApiClient = mockk()
    private val userRepository: UserRepository = mockk()
    private val reducer = OnboardingReducer(loginApi, userRepository, dispatcherProvider)

    private fun OnboardingState.withCredentials(
        email: Email = Email.from(""),
        password: Password = Password.from("")
    ): OnboardingState =
        copy(localState = OnboardingState.LocalState(email = email, password = password))

    @Test
    fun `does nothing if the email is invalid`() = runBlockingTest {
        val initialState = emptyState().withCredentials(password = validPassword)

        reducer.testReduceState(initialState, OnboardingAction.LoginTapped) { newState ->
            newState shouldBe initialState
        }
}
    @Test
    fun `does nothing if the password is invalid`() = runBlockingTest {
        val initialState = emptyState().withCredentials(email = validEmail)

        reducer.testReduceState(initialState, OnboardingAction.LoginTapped) { newState ->
            newState shouldBe initialState
        }
    }

    @Test
    fun `sets the users state to loading if both states are valid`() = runBlockingTest {
        val initialState =
            emptyState().withCredentials(email = validEmail, password = validPassword)

        reducer.testReduceState(initialState, OnboardingAction.LoginTapped) { newState ->
            newState shouldBe initialState.copy(
                user = Loadable.Loading
            )
        }
    }
}
