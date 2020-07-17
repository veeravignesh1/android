package com.toggl.domain.mappings

import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.onboarding.common.domain.OnboardingAction
import com.toggl.onboarding.common.domain.OnboardingState

fun mapAppStateToOnboardingState(appState: AppState): OnboardingState =
    OnboardingState(
        appState.user,
        appState.backStack,
        appState.onboardingLocalState
    )

fun mapOnboardingStateToAppState(appState: AppState, onboardingState: OnboardingState): AppState =
    appState.copy(
        user = onboardingState.user,
        backStack = onboardingState.backStack,
        onboardingLocalState = onboardingState.localState
    )

fun mapOnboardingActionToAppAction(onboardingAction: OnboardingAction): AppAction =
    AppAction.Onboarding(onboardingAction)
