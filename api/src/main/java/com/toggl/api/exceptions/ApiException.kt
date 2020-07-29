package com.toggl.api.exceptions

abstract class ApiException(val errorMessage: String) : Exception() {
    private val badJsonLocalisedError = "Encountered unexpected error."

    companion object {
        fun from(statusCode: Int, localizedErrorMessage: String?) =
            when (statusCode) {
                // Client errors
                ApiDeprecatedException.httpCode -> ApiDeprecatedException(localizedErrorMessage)
                BadRequestException.httpCode -> BadRequestException(localizedErrorMessage)
                ClientDeprecatedException.httpCode -> ClientDeprecatedException(localizedErrorMessage)
                ForbiddenException.httpCode -> ForbiddenException(localizedErrorMessage)
                NotFoundException.httpCode -> NotFoundException(localizedErrorMessage)
                PaymentRequiredException.httpCode -> PaymentRequiredException(localizedErrorMessage)
                RequestEntityTooLargeException.httpCode -> RequestEntityTooLargeException(localizedErrorMessage)
                TooManyRequestsException.httpCode -> TooManyRequestsException(localizedErrorMessage)
                UnauthorizedException.httpCode -> UnauthorizedException(localizedErrorMessage)

                // Server errors
                InternalServerErrorException.httpCode -> InternalServerErrorException(localizedErrorMessage)
                NotImplementedException.httpCode -> NotImplementedException(localizedErrorMessage)
                BadGatewayException.httpCode -> BadGatewayException(localizedErrorMessage)
                ServiceUnavailableException.httpCode -> ServiceUnavailableException(localizedErrorMessage)
                GatewayTimeoutException.httpCode -> GatewayTimeoutException(localizedErrorMessage)
                HttpVersionNotSupportedException.httpCode -> HttpVersionNotSupportedException(localizedErrorMessage)

                else -> UnknownApiErrorException(statusCode, localizedErrorMessage)
            }
    }
}