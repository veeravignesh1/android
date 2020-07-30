package com.toggl.timer.exceptions

private const val errorMessage = "Action cannot be performed when editableTimeEntry does not have a duration"
class EditableTimeEntryDoesNotHaveADurationSetException : IllegalStateException(errorMessage)
