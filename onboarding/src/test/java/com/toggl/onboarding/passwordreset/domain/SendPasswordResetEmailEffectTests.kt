package com.toggl.onboarding.passwordreset.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.api.exceptions.BadRequestException
import com.toggl.api.exceptions.InternalServerErrorException
import com.toggl.api.exceptions.OfflineException
import com.toggl.architecture.Failure
import com.toggl.models.validation.Email
import com.toggl.onboarding.common.CoroutineTest
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("The SendPasswordResetEmail effect")
class SendPasswordResetEmailEffectTests : CoroutineTest() {

    companion object {
        private const val offlineMessage = "offlineMessage"
        private const val genericErrorMessage = "genericErrorMessage"
        private const val emailDoesNotExistMessage = "emailDoesNotExistMessage"
        private val errorMessages = SendPasswordResetEmailEffect.ErrorMessages(offlineMessage, genericErrorMessage, emailDoesNotExistMessage)

        @JvmStatic
        fun `returns the adequate error as a failure`() = Stream.of(
            Failure(BadRequestException(""), emailDoesNotExistMessage),
            Failure(OfflineException(), offlineMessage),
            Failure(InternalServerErrorException("123"), "123"),
            Failure(IllegalStateException(), genericErrorMessage)
        )
    }

    private val authenticationApiClient = mockk<AuthenticationApiClient>()
    private val email = mockk<Email.Valid>()
    private val effect = SendPasswordResetEmailEffect(
        authenticationApiClient,
        dispatcherProvider,
        errorMessages,
        email
    )

    @Test
    fun `Forwards the api success message as an action`() = runBlockingTest {
        val resultString = "yay"
        coEvery { authenticationApiClient.resetPassword(email) } returns resultString

        val result = effect.execute() as PasswordResetAction.PasswordResetEmailSent
        result.message shouldBe resultString
    }

    @ParameterizedTest
    @MethodSource
    fun `returns the adequate error as a failure`(failure: Failure) = runBlockingTest {
        coEvery { authenticationApiClient.resetPassword(email) } throws failure.throwable

        val result = effect.execute() as PasswordResetAction.PasswordResetEmailFailed
        result.failure shouldBe failure
    }
}
