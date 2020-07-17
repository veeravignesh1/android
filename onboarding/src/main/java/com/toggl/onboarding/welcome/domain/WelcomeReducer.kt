package com.toggl.onboarding.welcome.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import javax.inject.Inject

class WelcomeReducer @Inject constructor() : Reducer<WelcomeState, WelcomeAction> {

    override fun reduce(
        state: MutableValue<WelcomeState>,
        action: WelcomeAction
    ): List<Effect<WelcomeAction>> =
        when (action) {
            WelcomeAction.ContinueWithEmailButtonTapped -> TODO()
            WelcomeAction.ContinueWithGoogleButtonTapped -> TODO()
            WelcomeAction.LoginWithSsoButtonTapped -> TODO()
        }
}
