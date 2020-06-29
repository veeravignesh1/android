package com.toggl.settings.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toggl.settings.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val store: SettingsStoreViewModel by viewModels()
}
