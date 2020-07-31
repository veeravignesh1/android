package com.toggl.onboarding.sso.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.onboarding.sso.domain.SsoAction
import com.toggl.onboarding.sso.domain.SsoState
import javax.inject.Inject

class SsoStoreViewModel @Inject constructor(
    store: Store<SsoState, SsoAction>
) : ViewModel(), Store<SsoState, SsoAction> by store