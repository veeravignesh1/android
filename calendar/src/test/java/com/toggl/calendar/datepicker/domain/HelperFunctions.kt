package com.toggl.calendar.datepicker.domain

import com.toggl.common.services.time.TimeService
import com.toggl.models.domain.UserPreferences
import io.mockk.mockk
import java.time.OffsetDateTime

fun createInitialState(
    selectedDate: OffsetDateTime = OffsetDateTime.parse("2020-02-20T20:20:20+00:00"),
    userPreferences: UserPreferences = UserPreferences.default
) = CalendarDatePickerState(
    selectedDate,
    userPreferences
)

fun createReducer(
    timeService: TimeService = mockk()
) = CalendarDatePickerReducer(
    timeService
)
