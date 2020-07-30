package com.toggl.api.exceptions

class ServiceUnavailableException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 503
        const val defaultErrorMessage = "Service unavailable."
    }
}
