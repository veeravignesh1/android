package com.toggl.onboarding.domain.reducers

import com.toggl.api.login.LoginApi
import com.toggl.architecture.Failure
import com.toggl.architecture.Loadable.Error
import com.toggl.architecture.Loadable.Loaded
import com.toggl.architecture.Loadable.Loading
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.models.validation.toEmail
import com.toggl.models.validation.toPassword
import com.toggl.onboarding.domain.actions.OnboardingAction
import com.toggl.onboarding.domain.effects.LogUserInEffect
import com.toggl.onboarding.domain.states.OnboardingState
import com.toggl.onboarding.domain.states.email
import com.toggl.onboarding.domain.states.password
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingReducer @Inject constructor(private val api: LoginApi) : Reducer<OnboardingState, OnboardingAction> {

    override fun reduce(state: SettableValue<OnboardingState>, action: OnboardingAction): List<Effect<OnboardingAction>> {

        val currentState = state.value

        return when (action) {
            OnboardingAction.LoginTapped -> {
                when (val email = currentState.email) {
                    is Email.Invalid -> noEffect()
                    is Email.Valid -> when (val password = currentState.password) {
                        is Password.Invalid -> noEffect()
                        is Password.Valid -> {
                            state.value = currentState.copy(user = Loading())
                            effect(LogUserInEffect(api, email, password))
                        }
                    }
                }
            }
            is OnboardingAction.SetUser -> {
                state.value = currentState.copy(user = Loaded(action.user))
                noEffect()
            }
            is OnboardingAction.SetUserError -> {
                state.value = currentState.copy(user = Error(Failure(action.throwable, "")))
                noEffect()
            }
            is OnboardingAction.EmailEntered -> {
                val newLocalState = currentState.localState.copy(email = action.email.toEmail())
                state.value = currentState.copy(localState = newLocalState)
                noEffect()
            }
            is OnboardingAction.PasswordEntered -> {
                val newLocalState =
                    currentState.localState.copy(password = action.password.toPassword())
                state.value = currentState.copy(localState = newLocalState)
                noEffect()
            }
        }
    }
}
