package com.toggl.api.exceptions

class HttpVersionNotSupportedException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 505
        const val defaultErrorMessage = "HTTP version is not supported."
    }
}
