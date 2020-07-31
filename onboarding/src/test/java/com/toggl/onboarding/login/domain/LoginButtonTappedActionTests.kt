package com.toggl.onboarding.login.domain

import com.toggl.architecture.Loadable
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import com.toggl.onboarding.common.validEmail
import com.toggl.onboarding.common.validPassword
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("The login tapped action")
class LoginButtonTappedActionTests : CoroutineTest() {
    private val reducer = createLoginReducer(dispatcherProvider = dispatcherProvider)

    private fun LoginState.withCredentials(
        email: Email = Email.from(""),
        password: Password = Password.from("")
    ) = copy(email = email, password = password)

    @Test
    fun `does nothing if the email is invalid`() = runBlockingTest {
        val initialState = emptyLoginState().withCredentials(password = validPassword)

        reducer.testReduceState(initialState, LoginAction.LoginButtonTapped) { newState ->
            newState shouldBe initialState
        }
    }
    @Test
    fun `does nothing if the password is invalid`() = runBlockingTest {
        val initialState = emptyLoginState().withCredentials(email = validEmail)

        reducer.testReduceState(initialState, LoginAction.LoginButtonTapped) { newState ->
            newState shouldBe initialState
        }
    }

    @Test
    fun `sets the users state to loading if both states are valid`() = runBlockingTest {
        val initialState =
            emptyLoginState().withCredentials(email = validEmail, password = validPassword)

        reducer.testReduceState(initialState, LoginAction.LoginButtonTapped) { newState ->
            newState shouldBe initialState.copy(
                user = Loadable.Loading
            )
        }
    }
}
