package com.toggl.onboarding.passwordreset.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import javax.inject.Inject

class PasswordResetReducer @Inject constructor() : Reducer<PasswordResetState, PasswordResetAction> {

    override fun reduce(
        state: MutableValue<PasswordResetState>,
        action: PasswordResetAction
    ): List<Effect<PasswordResetAction>> =
        when (action) {
            PasswordResetAction.SendEmailButtonTapped -> TODO()
            is PasswordResetAction.EmailEntered -> TODO()
        }
}
