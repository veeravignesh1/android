package com.toggl.api.exceptions

abstract class ServerErrorException(errorMessage: String) : ApiException(errorMessage)
