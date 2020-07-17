package com.toggl.onboarding.passwordreset.domain

import arrow.optics.optics

@optics
sealed class PasswordResetAction {
    object SendEmailButtonTapped : PasswordResetAction()
    data class EmailEntered(val email: String) : PasswordResetAction()

    companion object
}

fun PasswordResetAction.formatForDebug() =
    when (this) {
        PasswordResetAction.SendEmailButtonTapped -> "Send password reset email button tapped"
        is PasswordResetAction.EmailEntered -> "Email entered $email"
    }