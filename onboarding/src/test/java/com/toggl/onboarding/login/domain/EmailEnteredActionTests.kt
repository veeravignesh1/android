package com.toggl.onboarding.login.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import com.toggl.onboarding.common.validEmail
import com.toggl.repository.interfaces.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The email entered action")
class EmailEnteredActionTests : CoroutineTest() {
    private val authenticationApi: AuthenticationApiClient = mockk()
    private val userRepository: UserRepository = mockk()
    private val reducer =
        LoginReducer(authenticationApi, userRepository, dispatcherProvider)

    @Test
    fun `sets the email in the state`() = runBlocking {
        reducer.testReduceState(emptyState(), LoginAction.EmailEntered(validEmail.toString())) { newState ->
            newState.email.email shouldBe validEmail.email
        }
    }
}
