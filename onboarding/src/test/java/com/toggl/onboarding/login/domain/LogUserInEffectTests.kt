package com.toggl.onboarding.login.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.validEmail
import com.toggl.onboarding.common.validPassword
import com.toggl.onboarding.common.validUser
import com.toggl.repository.interfaces.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("The log user in effect")
class LogUserInEffectTests : CoroutineTest() {
    private val apiClient: AuthenticationApiClient = mockk { coEvery { login(validEmail, validPassword) } returns validUser }
    private val userRepository: UserRepository = mockk { coEvery { set(validUser) } returns Unit }
    private val errorMessages = LogUserInEffect.ErrorMessages("", "", "", "", "")

    private suspend fun executeEffect() =
        LogUserInEffect(
            apiClient,
            userRepository,
            dispatcherProvider,
            errorMessages,
            validEmail,
            validPassword
        ).execute()

    @Test
    fun `sets the user`() = runBlockingTest {
        executeEffect()

        coVerify { userRepository.set(validUser) }
    }

    @Test
    fun `return a set user action`() = runBlockingTest {
        val action = executeEffect()

        action.shouldBeInstanceOf<LoginAction.SetUser> { it.user shouldBe validUser }
    }

    @Test
    fun `return a set error action if the api call throws`() = runBlockingTest {
        coEvery { apiClient.login(validEmail, validPassword) } throws IllegalStateException()

        val action = executeEffect()

        action.shouldBeInstanceOf<LoginAction.SetUserError>()
    }
}
