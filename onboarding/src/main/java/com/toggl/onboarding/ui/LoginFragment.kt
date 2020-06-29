package com.toggl.onboarding.ui

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.toggl.architecture.Loadable
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password
import com.toggl.onboarding.R
import com.toggl.onboarding.domain.actions.OnboardingAction
import com.toggl.onboarding.domain.states.email
import com.toggl.onboarding.domain.states.password
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {

    private val store: LoginViewModel by viewModels()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_button.setOnClickListener {
            store.dispatch(OnboardingAction.LoginTapped)
        }

        email.doOnTextChanged { text, _, _, _ ->
            val action = OnboardingAction.EmailEntered(text.toString())
            store.dispatch(action)
        }

        password.doOnTextChanged { text, _, _, _ ->
            val action = OnboardingAction.PasswordEntered(text.toString())
            store.dispatch(action)
        }

        lifecycleScope.launch {
            store.state
                .map { it.email is Email.Valid && it.password is Password.Valid && it.user !is Loadable.Loading }
                .distinctUntilChanged()
                .onEach { login_button.isEnabled = it }
                .launchIn(this)
        }
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}
