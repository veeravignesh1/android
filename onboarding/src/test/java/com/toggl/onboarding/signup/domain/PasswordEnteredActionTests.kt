package com.toggl.onboarding.signup.domain

import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import com.toggl.onboarding.common.validPassword
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("The password entered action")
class PasswordEnteredActionTests : CoroutineTest() {
    private val reducer = createSignUpReducer()

    @Test
    fun `sets the password`() = runBlockingTest {
        val initialState = emptySignUpState()

        reducer.testReduceState(initialState, SignUpAction.PasswordEntered(validPassword.toString())) { newState ->
            newState shouldBe initialState.copy(password = validPassword)
        }
    }
}
