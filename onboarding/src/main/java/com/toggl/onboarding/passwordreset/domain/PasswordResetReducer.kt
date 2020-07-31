package com.toggl.onboarding.passwordreset.domain

import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.Loadable
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.effectOf
import com.toggl.architecture.extensions.noEffect
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.extensions.returnEffect
import com.toggl.common.feature.navigation.popBackStack
import com.toggl.models.validation.Email
import com.toggl.models.validation.toEmail
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordResetReducer @Inject constructor(
    private val authenticationApiClient: AuthenticationApiClient,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMessages: SendPasswordResetEmailEffect.ErrorMessages
) : Reducer<PasswordResetState, PasswordResetAction> {

    override fun reduce(
        state: MutableValue<PasswordResetState>,
        action: PasswordResetAction
    ): List<Effect<PasswordResetAction>> =
        when (action) {
            is PasswordResetAction.EmailEntered -> state.mutateWithoutEffects { copy(email = action.email.toEmail()) }
            PasswordResetAction.CloseButtonTapped -> state.popBackStack().mutateWithoutEffects {
                copy(resetPasswordResult = Loadable.Uninitialized)
            }
            is PasswordResetAction.PasswordResetEmailSent -> state.mutate {
                copy(resetPasswordResult = Loadable.Loaded(action.message))
            } returnEffect effectOf(PasswordResetAction.CloseButtonTapped)
            is PasswordResetAction.PasswordResetEmailFailed -> state.mutateWithoutEffects {
                copy(resetPasswordResult = Loadable.Error(action.failure))
            }
            PasswordResetAction.SendEmailButtonTapped -> {
                val email = state.mapState { email as? Email.Valid }
                if (email == null) noEffect()
                else sendPasswordResetEmailEffect(email)
            }
        }

    private fun sendPasswordResetEmailEffect(email: Email.Valid) = effect(
        SendPasswordResetEmailEffect(
            authenticationApiClient,
            dispatcherProvider,
            errorMessages,
            email
        )
    )
}
