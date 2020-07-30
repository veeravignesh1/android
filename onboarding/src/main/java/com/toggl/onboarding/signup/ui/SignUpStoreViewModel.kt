package com.toggl.onboarding.signup.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.onboarding.signup.domain.SignUpAction
import com.toggl.onboarding.signup.domain.SignUpState
import javax.inject.Inject

class SignUpStoreViewModel @Inject constructor(
    store: Store<SignUpState, SignUpAction>
) : ViewModel(), Store<SignUpState, SignUpAction> by store
