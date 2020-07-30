package com.toggl.api.exceptions

class GatewayTimeoutException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 504
        const val defaultErrorMessage = "Gateway timeout."
    }
}
