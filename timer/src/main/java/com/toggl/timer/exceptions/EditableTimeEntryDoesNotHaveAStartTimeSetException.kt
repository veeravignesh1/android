package com.toggl.timer.exceptions

private const val errorMessage = "Action cannot be performed when editableTimeEntry does not have a start time"
class EditableTimeEntryDoesNotHaveAStartTimeSetException : IllegalStateException(errorMessage)
