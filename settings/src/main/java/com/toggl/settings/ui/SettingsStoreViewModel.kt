package com.toggl.settings.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsState

class SettingsStoreViewModel @ViewModelInject constructor(
    store: Store<SettingsState, SettingsAction>
) : ViewModel(), Store<SettingsState, SettingsAction> by store