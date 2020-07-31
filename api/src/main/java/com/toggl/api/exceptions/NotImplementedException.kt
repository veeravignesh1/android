package com.toggl.api.exceptions

class NotImplementedException(localizedErrorMessage: String?) : ServerErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 501
        const val defaultErrorMessage = "This feature is not implemented."
    }
}
