package com.toggl.onboarding.domain.states

import arrow.optics.optics
import com.toggl.architecture.Loadable
import com.toggl.common.feature.navigation.BackStack
import com.toggl.models.domain.User
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password

@optics
data class OnboardingState(
    val user: Loadable<User>,
    val backStack: BackStack,
    val localState: LocalState
) {
    data class LocalState internal constructor(
        internal val email: Email,
        internal val password: Password
    ) {
        constructor() : this(Email.Invalid(""), Password.Invalid(""))

        companion object
    }

    companion object
}

internal val OnboardingState.email: Email
    get() = localState.email

internal val OnboardingState.password: Password
    get() = localState.password
