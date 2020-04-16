package com.toggl.onboarding.domain.reducers

import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.api.login.LoginApi
import com.toggl.architecture.Failure
import com.toggl.architecture.Loadable.Error
import com.toggl.architecture.Loadable.Loaded
import com.toggl.architecture.Loadable.Loading
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.MutableValue
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
import com.toggl.onboarding.domain.states.localState
import com.toggl.onboarding.domain.states.password
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingReducer @Inject constructor(private val api: LoginApi) : Reducer<OnboardingState, OnboardingAction> {

    override fun reduce(state: MutableValue<OnboardingState>, action: OnboardingAction): List<Effect<OnboardingAction>> {
        return when (action) {
            OnboardingAction.LoginTapped -> {
                val currentState = state()
                when (val email = currentState.email) {
                    is Email.Invalid -> noEffect()
                    is Email.Valid -> when (val password = currentState.password) {
                        is Password.Invalid -> noEffect()
                        is Password.Valid -> {
                            state.mutate { copy(user = Loading()) }
                            effect(LogUserInEffect(api, email, password))
                        }
                    }
                }
            }
            is OnboardingAction.SetUser ->
                state.mutateWithoutEffects { copy(user = Loaded(action.user)) }
            is OnboardingAction.SetUserError ->
                state.mutateWithoutEffects { copy(user = Error(Failure(action.throwable, ""))) }
            is OnboardingAction.EmailEntered ->
                state.mutateWithoutEffects {
                    OnboardingState.localState.modify(this) {
                        it.copy(email = action.email.toEmail())
                    }
                }
            is OnboardingAction.PasswordEntered ->
                state.mutateWithoutEffects {
                    OnboardingState.localState.modify(this) {
                        it.copy(password = action.password.toPassword())
                    }
                }
        }
    }
}
