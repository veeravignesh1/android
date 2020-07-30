package com.toggl.api.exceptions

abstract class ClientErrorException(errorMessage: String) : ApiException(errorMessage)