package com.toggl.onboarding.login.domain

import com.toggl.architecture.Loadable
import com.toggl.onboarding.domain.states.OnboardingState

fun emptyState() =
    OnboardingState(Loadable.Uninitialized, emptyList(), OnboardingState.LocalState())