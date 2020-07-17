package com.toggl.onboarding.signup.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toggl.onboarding.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private val store: SignUpStoreViewModel by viewModels()
}
