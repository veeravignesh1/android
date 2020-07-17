package com.toggl.onboarding.sso.domain

import arrow.optics.optics
import com.toggl.models.validation.Email
import com.toggl.onboarding.common.domain.OnboardingState
import com.toggl.onboarding.common.domain.localState

@optics
data class SsoState(val email: Email) {
    companion object {
        fun fromOnboardingState(onboardingState: OnboardingState) =
            SsoState(onboardingState.localState.email)

        fun toOnboardingState(onboardingState: OnboardingState, ssoState: SsoState) =
            onboardingState.copy(
                localState = onboardingState.localState.copy(
                    email = ssoState.email
                )
            )
    }
}