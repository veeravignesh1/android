package com.toggl.onboarding.passwordreset.domain

import com.toggl.architecture.Failure

sealed class PasswordResetAction {
    object SendEmailButtonTapped : PasswordResetAction()
    data class EmailEntered(val email: String) : PasswordResetAction()
    object CloseButtonTapped : PasswordResetAction()
    data class PasswordResetEmailSent(val message: String) : PasswordResetAction()
    data class PasswordResetEmailFailed(val failure: Failure) : PasswordResetAction()

    companion object
}

fun PasswordResetAction.formatForDebug() =
    when (this) {
        PasswordResetAction.SendEmailButtonTapped -> "Send password reset email button tapped"
        is PasswordResetAction.EmailEntered -> "Email entered $email"
        PasswordResetAction.CloseButtonTapped -> "Close button tapped"
        is PasswordResetAction.PasswordResetEmailSent -> "Password reset email sent"
        is PasswordResetAction.PasswordResetEmailFailed -> "Failed to send password reset email with error ${failure.errorMessage}"
    }
