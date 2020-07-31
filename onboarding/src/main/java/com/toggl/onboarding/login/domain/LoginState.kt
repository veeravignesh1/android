package com.toggl.onboarding.login.domain

import com.toggl.architecture.Loadable
import com.toggl.common.feature.navigation.BackStack
import com.toggl.common.feature.navigation.BackStackAwareState
import com.toggl.common.feature.navigation.pop
import com.toggl.models.domain.User
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.common.domain.OnboardingState

data class LoginState(
    val user: Loadable<User>,
    val backStack: BackStack,
    val email: Email,
    val password: Password
) : BackStackAwareState<LoginState> {

    override fun popBackStack(): LoginState =
        copy(backStack = backStack.pop())

    companion object {
        fun fromOnboardingState(onboardingState: OnboardingState) =
            LoginState(
                onboardingState.user,
                onboardingState.backStack,
                onboardingState.localState.email,
                onboardingState.localState.password
            )

        fun toOnboardingState(onboardingState: OnboardingState, loginState: LoginState) =
            onboardingState.copy(
                user = loginState.user,
                backStack = loginState.backStack,
                localState = onboardingState.localState.copy(
                    email = loginState.email,
                    password = loginState.password
                )
            )
    }
}
