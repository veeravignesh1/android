package com.toggl.timer.exceptions

import java.lang.IllegalStateException

private const val errorMessage = "Action cannot be performed when editableProject is not set"
class EditableProjectShouldNotBeNullException : IllegalStateException(errorMessage)