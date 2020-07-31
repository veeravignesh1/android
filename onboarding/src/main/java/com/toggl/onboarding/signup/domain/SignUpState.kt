package com.toggl.onboarding.signup.domain

import com.toggl.common.feature.navigation.BackStack
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.common.domain.OnboardingState

data class SignUpState(
    val email: Email,
    val password: Password,
    val backStack: BackStack
) {
    companion object {
        fun fromOnboardingState(onboardingState: OnboardingState) =
            SignUpState(
                onboardingState.localState.email,
                onboardingState.localState.password,
                onboardingState.backStack
            )

        fun toOnboardingState(onboardingState: OnboardingState, signUpState: SignUpState) =
            onboardingState.copy(
                backStack = signUpState.backStack,
                localState = onboardingState.localState.copy(
                    email = signUpState.email,
                    password = signUpState.password
                )
            )
    }
}