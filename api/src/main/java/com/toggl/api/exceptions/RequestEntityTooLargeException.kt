package com.toggl.api.exceptions

class RequestEntityTooLargeException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 413
        const val defaultErrorMessage = "The payload is too large, split it into batches."
    }
}
