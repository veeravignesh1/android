package com.toggl.onboarding.signup.domain

sealed class SignUpAction {
    object SignUpButtonTapped : SignUpAction()
    object GoToLoginTapped : SignUpAction()
    data class EmailEntered(val email: String) : SignUpAction()
    data class PasswordEntered(val password: String) : SignUpAction()

    companion object
}

fun SignUpAction.formatForDebug() =
    when (this) {
        SignUpAction.SignUpButtonTapped -> "Sign Up button tapped"
        is SignUpAction.EmailEntered -> "Email entered $email"
        is SignUpAction.PasswordEntered -> "Password entered $password"
        SignUpAction.GoToLoginTapped -> "Login button tapped"
    }