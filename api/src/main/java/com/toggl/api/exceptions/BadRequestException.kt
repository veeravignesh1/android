package com.toggl.api.exceptions

class BadRequestException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 400
        const val defaultErrorMessage = "The data is not valid or acceptable."
    }
}