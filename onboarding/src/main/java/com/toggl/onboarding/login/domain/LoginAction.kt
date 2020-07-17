package com.toggl.onboarding.login.domain

import com.toggl.models.domain.User

sealed class LoginAction {
    object LoginButtonTapped : LoginAction()
    data class EmailEntered(val email: String) : LoginAction()
    data class PasswordEntered(val password: String) : LoginAction()
    data class SetUser(val user: User) : LoginAction()
    data class SetUserError(val throwable: Throwable) : LoginAction()
}

fun LoginAction.formatForDebug(): String =
    when (this) {
        LoginAction.LoginButtonTapped -> "Login button tapped"
        is LoginAction.EmailEntered -> "Email entered $email"
        is LoginAction.PasswordEntered -> "Password entered $password"
        is LoginAction.SetUser -> "Setting user $user"
        is LoginAction.SetUserError -> "Setting user error $throwable"
    }
