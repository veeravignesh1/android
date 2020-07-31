package com.toggl.api.exceptions

private const val defaultErrorMessage = "The server responded with an unexpected HTTP status code."
class UnknownApiErrorException(
    val httpCode: Int,
    localizedErrorMessage: String?
) : ApiException(localizedErrorMessage ?: defaultErrorMessage)