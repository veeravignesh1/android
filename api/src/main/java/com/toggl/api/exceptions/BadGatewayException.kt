package com.toggl.api.exceptions

class BadGatewayException(localizedErrorMessage: String?) : ServerErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 502
        const val defaultErrorMessage = "Bad gateway."
    }
}
