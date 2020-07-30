package com.toggl.api.exceptions

class ClientDeprecatedException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 418
        const val defaultErrorMessage = "This version of client application is deprecated and must be updated to an up-to-date version."
    }
}