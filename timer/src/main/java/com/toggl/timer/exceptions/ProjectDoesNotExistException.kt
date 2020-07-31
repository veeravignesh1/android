package com.toggl.timer.exceptions

import java.lang.IllegalStateException

private const val errorMessage = "Action cannot be performed because the project is not on the state"
class ProjectDoesNotExistException : IllegalStateException(errorMessage)