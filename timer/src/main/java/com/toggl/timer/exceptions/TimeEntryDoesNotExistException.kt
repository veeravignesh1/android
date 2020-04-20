package com.toggl.timer.exceptions

import java.lang.IllegalStateException

private const val errorMessage = "Action cannot be performed because the time entry is not on the state"
class TimeEntryDoesNotExistException : IllegalStateException(errorMessage)