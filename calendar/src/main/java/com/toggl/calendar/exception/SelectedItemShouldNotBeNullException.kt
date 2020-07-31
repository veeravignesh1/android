package com.toggl.calendar.exception

private const val errorMessage = "Action cannot be performed on when the selected item is null"
class SelectedItemShouldNotBeNullException : IllegalStateException(errorMessage)
