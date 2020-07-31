package com.toggl.api.exceptions

class ApiDeprecatedException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 410
        const val defaultErrorMessage = "This version of API is deprecated and the client must be updated to an up-to-date version."
    }
}
