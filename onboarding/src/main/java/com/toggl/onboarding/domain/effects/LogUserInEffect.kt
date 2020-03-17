package com.toggl.onboarding.domain.effects

import com.toggl.api.login.LoginApi
import com.toggl.architecture.core.Effect
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.domain.actions.OnboardingAction

class LogUserInEffect(
    private val api: LoginApi,
    private val email: Email.Valid,
    private val password: Password.Valid
) : Effect<OnboardingAction> {
    override suspend fun execute(): OnboardingAction? =
        try {
            val user = api.login(email, password)
            OnboardingAction.SetUser(user)
        } catch (throwable: Throwable) {
            OnboardingAction.SetUserError(throwable)
        }
}