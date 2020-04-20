package com.toggl.timer.exceptions

import java.lang.IllegalStateException

private const val errorMessage = "Action cannot be performed when editableTimeEntry is not set"
class EditableTimeEntryShouldNotBeNullException : IllegalStateException(errorMessage)