package com.toggl.onboarding.signup.ui

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.toggl.common.extensions.doSafeAfterTextChanged
import com.toggl.common.extensions.setSafeText
import com.toggl.onboarding.R
import com.toggl.onboarding.signup.domain.SignUpAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private val store: SignUpStoreViewModel by viewModels()

    private lateinit var emailWatcher: TextWatcher
    private lateinit var passwordWatcher: TextWatcher

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailWatcher = email.doSafeAfterTextChanged { store.dispatch(SignUpAction.EmailEntered(it.toString())) }
        passwordWatcher = password.doSafeAfterTextChanged { store.dispatch(SignUpAction.PasswordEntered(it.toString())) }

        login_button.setOnClickListener {
            store.dispatch(SignUpAction.GoToLoginTapped)
        }
    }

    override fun onResume() {
        super.onResume()

        store.state
            .map { it.email }
            .onEach { email?.setSafeText(it.toString()) }
            .launchIn(lifecycleScope)

        store.state
            .map { it.password }
            .onEach { password?.setSafeText(it.toString()) }
            .launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        email?.removeTextChangedListener(emailWatcher)
        password?.removeTextChangedListener(passwordWatcher)

        super.onDestroyView()
    }
}
