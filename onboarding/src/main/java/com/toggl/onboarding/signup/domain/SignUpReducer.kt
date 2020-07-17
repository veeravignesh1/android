package com.toggl.onboarding.signup.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import javax.inject.Inject

class SignUpReducer @Inject constructor() : Reducer<SignUpState, SignUpAction> {

    override fun reduce(
        state: MutableValue<SignUpState>,
        action: SignUpAction
    ): List<Effect<SignUpAction>> =
        when (action) {
            SignUpAction.SignUpButtonTapped -> TODO()
            is SignUpAction.EmailEntered -> TODO()
            is SignUpAction.PasswordEntered -> TODO()
        }
}
