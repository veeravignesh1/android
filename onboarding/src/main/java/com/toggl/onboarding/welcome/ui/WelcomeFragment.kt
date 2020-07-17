package com.toggl.onboarding.welcome.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toggl.onboarding.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private val store: WelcomeStoreViewModel by viewModels()
}
