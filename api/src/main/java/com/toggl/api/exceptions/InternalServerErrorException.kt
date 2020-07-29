package com.toggl.api.exceptions

class InternalServerErrorException(localizedErrorMessage: String?) : ServerErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 500
        const val defaultErrorMessage = "Internal server error."
    }
}
