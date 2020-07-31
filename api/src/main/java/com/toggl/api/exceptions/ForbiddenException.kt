package com.toggl.api.exceptions

class ForbiddenException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 403
        const val defaultErrorMessage = "User cannot perform this request."
    }
}
