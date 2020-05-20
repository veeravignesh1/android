package com.toggl.calendar.domain

import arrow.optics.optics

@optics
data class CalendarState(
    val toBeDeleted: String
) {
    companion object
}