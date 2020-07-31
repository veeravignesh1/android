package com.toggl.onboarding.signup.domain

import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import com.toggl.onboarding.common.validEmail
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The email entered action")
class EmailEnteredActionTests : CoroutineTest() {
    private val reducer = createSignUpReducer()

    @Test
    fun `sets the email in the state`() = runBlocking {
        val initialState = emptySignUpState()

        reducer.testReduceState(initialState, SignUpAction.EmailEntered(validEmail.toString())) { newState ->
            newState.email.email shouldBe validEmail.email
        }
    }
}
