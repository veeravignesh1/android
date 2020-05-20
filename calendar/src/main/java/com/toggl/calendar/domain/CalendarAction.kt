package com.toggl.calendar.domain

sealed class CalendarAction {
    object ExampleAction : CalendarAction()

    companion object
}

fun CalendarAction.formatForDebug() =
    when (this) {
        is CalendarAction.ExampleAction -> "TODO: DELETE ME WHEN YOU ADD THE FIRST ACTUAL ACTION"
    }