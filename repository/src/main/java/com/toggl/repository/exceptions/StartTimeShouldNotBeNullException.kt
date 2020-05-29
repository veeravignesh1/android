package com.toggl.repository.exceptions

import java.lang.IllegalStateException

private const val errorMessage = "The start time should not be null"
class StartTimeShouldNotBeNullException : IllegalStateException(errorMessage)
