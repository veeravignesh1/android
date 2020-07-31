package com.toggl.timer.exceptions

import java.lang.IllegalStateException

private const val errorMessage = "Action cannot be performed because the tag is not on the state"
class TagDoesNotExistException : IllegalStateException(errorMessage)