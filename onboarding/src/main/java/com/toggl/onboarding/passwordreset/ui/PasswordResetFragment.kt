package com.toggl.onboarding.passwordreset.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toggl.onboarding.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetFragment : Fragment(R.layout.fragment_passwordreset) {
    private val store: PasswordResetStoreViewModel by viewModels()
}
