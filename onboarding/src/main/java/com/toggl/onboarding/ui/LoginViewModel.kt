package com.toggl.onboarding.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.onboarding.domain.actions.OnboardingAction
import com.toggl.onboarding.domain.states.OnboardingState

class LoginViewModel @ViewModelInject constructor(
    store: Store<OnboardingState, OnboardingAction>
) : ViewModel(), Store<OnboardingState, OnboardingAction> by store
