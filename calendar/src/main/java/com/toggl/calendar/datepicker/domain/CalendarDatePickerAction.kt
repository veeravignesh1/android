package com.toggl.calendar.datepicker.domain

sealed class CalendarDatePickerAction {
    object ExampleAction : CalendarDatePickerAction()
}

fun CalendarDatePickerAction.formatForDebug() =
    when (this) {
        is CalendarDatePickerAction.ExampleAction -> "Example Action"
    }