package com.toggl.onboarding.passwordreset.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.toggl.architecture.Loadable
import com.toggl.architecture.errorMessageOrEmptyString
import com.toggl.architecture.valueOrNull
import com.toggl.common.extensions.doSafeAfterTextChanged
import com.toggl.common.extensions.requestFocusAndShowKeyboard
import com.toggl.common.extensions.setSafeText
import com.toggl.models.validation.Email
import com.toggl.onboarding.R
import com.toggl.onboarding.passwordreset.domain.PasswordResetAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_passwordreset.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PasswordResetFragment : Fragment(R.layout.fragment_passwordreset) {
    private val store: PasswordResetStoreViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email.doSafeAfterTextChanged { store.dispatch(PasswordResetAction.EmailEntered(it.toString())) }
        send_email_button.setOnClickListener { store.dispatch(PasswordResetAction.SendEmailButtonTapped) }

        email.requestFocusAndShowKeyboard(activity)

        store.state
            .map { it.email }
            .onEach { email.setSafeText(it.toString()) }
            .launchIn(lifecycleScope)

        store.state
            .map { it.resetPasswordResult.errorMessageOrEmptyString() }
            .distinctUntilChanged()
            .onEach { email_label.error = it }
            .launchIn(lifecycleScope)

        store.state
            .map { it.email is Email.Valid && it.resetPasswordResult !is Loadable.Loading }
            .distinctUntilChanged()
            .onEach { sendButtonIsEnabled -> send_email_button.isEnabled = sendButtonIsEnabled }
            .launchIn(lifecycleScope)

        store.state
            .map { it.resetPasswordResult is Loadable.Loading }
            .distinctUntilChanged()
            .onEach { isLoading -> email.isEnabled = !isLoading }
            .launchIn(lifecycleScope)

        store.state
            .map { it.resetPasswordResult.valueOrNull() ?: "" }
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .onEach { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
            .launchIn(lifecycleScope)
    }
}
