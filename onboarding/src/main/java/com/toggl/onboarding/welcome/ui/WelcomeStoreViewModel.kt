package com.toggl.onboarding.welcome.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.onboarding.welcome.domain.WelcomeAction
import com.toggl.onboarding.welcome.domain.WelcomeState
import javax.inject.Inject

class WelcomeStoreViewModel @Inject constructor(
    store: Store<WelcomeState, WelcomeAction>
) : ViewModel(), Store<WelcomeState, WelcomeAction> by store