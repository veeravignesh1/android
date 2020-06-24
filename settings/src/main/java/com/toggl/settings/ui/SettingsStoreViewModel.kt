package com.toggl.settings.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsState
import javax.inject.Inject

class SettingsStoreViewModel @Inject constructor(
    store: Store<SettingsState, SettingsAction>
) : ViewModel(), Store<SettingsState, SettingsAction> by store