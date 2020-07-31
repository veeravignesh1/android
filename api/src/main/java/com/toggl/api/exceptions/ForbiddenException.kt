package com.toggl.api.exceptions

class ForbiddenException(
    localizedErrorMessage: String?,
    val remainingLoginAttempts: Int?
) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val remainingLoginAttemptsHeaderName = "X-Remaining-Login-Attempts"
        const val httpCode = 403
        const val defaultErrorMessage = "User cannot perform this request."
    }
}
