package com.toggl.api.exceptions

class UnauthorizedException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 401
        const val defaultErrorMessage = "User is not authorized to make this request and must enter login again."
    }
}
