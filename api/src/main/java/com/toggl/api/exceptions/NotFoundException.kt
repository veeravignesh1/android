package com.toggl.api.exceptions

class NotFoundException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 404
        const val defaultErrorMessage = "The resource was not found."
    }
}
