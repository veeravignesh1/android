package com.toggl.onboarding.signup.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.onboarding.signup.domain.SignUpAction
import com.toggl.onboarding.signup.domain.SignUpState

class SignUpStoreViewModel @ViewModelInject constructor(
    store: Store<SignUpState, SignUpAction>
) : ViewModel(), Store<SignUpState, SignUpAction> by store