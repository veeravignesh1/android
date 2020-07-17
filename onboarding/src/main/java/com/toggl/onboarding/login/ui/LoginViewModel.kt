package com.toggl.onboarding.login.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.onboarding.login.domain.LoginAction
import com.toggl.onboarding.login.domain.LoginState

class LoginViewModel @ViewModelInject constructor(
    store: Store<LoginState, LoginAction>
) : ViewModel(), Store<LoginState, LoginAction> by store
