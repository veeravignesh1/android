package com.toggl.onboarding.passwordreset.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.onboarding.passwordreset.domain.PasswordResetAction
import com.toggl.onboarding.passwordreset.domain.PasswordResetState
import javax.inject.Inject

class PasswordResetStoreViewModel @Inject constructor(
    store: Store<PasswordResetState, PasswordResetAction>
) : ViewModel(), Store<PasswordResetState, PasswordResetAction> by store
