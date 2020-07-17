package com.toggl.onboarding.domain.effects

import com.toggl.api.login.LoginApiClient
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.domain.actions.OnboardingAction
import com.toggl.repository.interfaces.UserRepository
import kotlinx.coroutines.withContext

class LogUserInEffect(
    private val apiClient: LoginApiClient,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val email: Email.Valid,
    private val password: Password.Valid
) : Effect<OnboardingAction> {
    override suspend fun execute(): OnboardingAction? = withContext(dispatcherProvider.io) {
        try {
            val user = apiClient.login(email, password)
            userRepository.set(user)
            OnboardingAction.SetUser(user)
        } catch (throwable: Throwable) {
            OnboardingAction.SetUserError(throwable)
        }
    }
}