package com.toggl.onboarding.signup.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.backStackOf
import com.toggl.models.validation.toEmail
import com.toggl.models.validation.toPassword
import javax.inject.Inject

class SignUpReducer @Inject constructor() : Reducer<SignUpState, SignUpAction> {

    override fun reduce(
        state: MutableValue<SignUpState>,
        action: SignUpAction
    ): List<Effect<SignUpAction>> =
        when (action) {
            SignUpAction.SignUpButtonTapped -> TODO()
            is SignUpAction.EmailEntered -> state.mutateWithoutEffects { copy(email = action.email.toEmail()) }
            is SignUpAction.PasswordEntered -> state.mutateWithoutEffects { copy(password = action.password.toPassword()) }
            SignUpAction.GoToLoginTapped -> state.mutateWithoutEffects {
                copy(backStack = backStackOf(Route.Welcome, Route.Login))
            }
        }
}
