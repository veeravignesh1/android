package com.toggl.calendar.exception

private const val errorMessage = "Action cannot be performed when selectedItem is not a time entry"
class SelectedItemShouldBeATimeEntryException : IllegalStateException(errorMessage)