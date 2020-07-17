package com.toggl.onboarding.signup.domain

import arrow.optics.optics
import com.toggl.models.validation.Email
import com.toggl.onboarding.common.domain.OnboardingState

@optics
data class SignUpState(val email: Email) {
    companion object {
        fun fromOnboardingState(onboardingState: OnboardingState) =
            SignUpState(onboardingState.localState.email)

        fun toOnboardingState(onboardingState: OnboardingState, signUpState: SignUpState) =
            onboardingState.copy(
                localState = onboardingState.localState.copy(
                    email = signUpState.email
                )
            )
    }
}