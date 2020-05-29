package com.toggl.common.feature.timeentry.exceptions

import java.lang.IllegalStateException

private const val errorMessage = "Action cannot be performed on a stopped time entry"
class TimeEntryShouldNotBeStoppedException : IllegalStateException(errorMessage)