package com.toggl.onboarding.sso.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import javax.inject.Inject

class SsoReducer @Inject constructor() : Reducer<SsoState, SsoAction> {

    override fun reduce(
        state: MutableValue<SsoState>,
        action: SsoAction
    ): List<Effect<SsoAction>> =
        when (action) {
            SsoAction.ContinueButtonTapped -> TODO()
            is SsoAction.EmailEntered -> TODO()
        }
}
