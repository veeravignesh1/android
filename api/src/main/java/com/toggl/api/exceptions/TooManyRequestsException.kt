package com.toggl.api.exceptions

class TooManyRequestsException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 429
        const val defaultErrorMessage = "The rate limiting does not work properly, fix it."
    }
}
