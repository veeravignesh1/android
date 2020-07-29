package com.toggl.onboarding.login.ui

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.toggl.architecture.Loadable
import com.toggl.common.extensions.doSafeAfterTextChanged
import com.toggl.common.extensions.requestFocusAndShowKeyboard
import com.toggl.common.extensions.setSafeText
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.R
import com.toggl.onboarding.login.domain.LoginAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val store: LoginViewModel by viewModels()

    private lateinit var emailWatcher: TextWatcher
    private lateinit var passwordWatcher: TextWatcher

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_button.setOnClickListener { store.dispatch(LoginAction.LoginButtonTapped) }
        password_reset.setOnClickListener { store.dispatch(LoginAction.ForgotPasswordTapped) }
        emailWatcher = email.doSafeAfterTextChanged { store.dispatch(LoginAction.EmailEntered(it.toString())) }
        passwordWatcher = password.doSafeAfterTextChanged { store.dispatch(LoginAction.PasswordEntered(it.toString())) }

        email.requestFocusAndShowKeyboard(activity)
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

        store.state
            .map { it.email is Email.Valid && it.password is Password.Valid && it.user !is Loadable.Loading }
            .distinctUntilChanged()
            .onEach { login_button.isEnabled = it }
            .launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        email?.removeTextChangedListener(emailWatcher)
        password?.removeTextChangedListener(passwordWatcher)

        super.onDestroyView()
    }
}
