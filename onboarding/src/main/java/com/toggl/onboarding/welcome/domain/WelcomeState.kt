package com.toggl.onboarding.welcome.domain

import arrow.optics.optics
import com.toggl.onboarding.common.domain.OnboardingState

@optics
data class WelcomeState(
    val exampleText: String
) {
    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun fromOnboardingState(onboardingState: OnboardingState) =
            WelcomeState("")

        @Suppress("UNUSED_PARAMETER")
        fun toOnboardingState(onboardingState: OnboardingState, welcomeState: WelcomeState) =
            onboardingState
    }
}