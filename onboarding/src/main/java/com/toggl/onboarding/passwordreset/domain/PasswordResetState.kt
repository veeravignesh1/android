package com.toggl.onboarding.passwordreset.domain

import com.toggl.architecture.Loadable
import com.toggl.common.feature.navigation.BackStack
import com.toggl.common.feature.navigation.BackStackAwareState
import com.toggl.common.feature.navigation.pop
import com.toggl.models.validation.Email
import com.toggl.onboarding.common.domain.OnboardingState

data class PasswordResetState(
    val email: Email,
    val backStack: BackStack,
    val resetPasswordResult: Loadable<String>
) : BackStackAwareState<PasswordResetState> {

    override fun popBackStack(): PasswordResetState =
        copy(backStack = backStack.pop())

    companion object {
        fun fromOnboardingState(onboardingState: OnboardingState) =
            PasswordResetState(
                email = onboardingState.localState.email,
                backStack = onboardingState.backStack,
                resetPasswordResult = onboardingState.localState.resetPasswordResult
            )

        fun toOnboardingState(onboardingState: OnboardingState, passwordResetState: PasswordResetState) =
            onboardingState.copy(
                backStack = passwordResetState.backStack,
                localState = onboardingState.localState.copy(
                    email = passwordResetState.email,
                    resetPasswordResult = passwordResetState.resetPasswordResult
                )
            )
    }
}