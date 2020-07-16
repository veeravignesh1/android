package com.toggl.calendar.datepicker.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.common.extensions.toList
import com.toggl.common.services.time.TimeService
import com.toggl.repository.interfaces.SettingsRepository
import io.mockk.mockk
import java.time.OffsetDateTime

fun createInitialState(
    selectedDate: OffsetDateTime = OffsetDateTime.parse("2020-02-20T20:20:20+00:00"),
    availableDates: List<OffsetDateTime> = (selectedDate.minusDays(7)..selectedDate.plusDays(7)).toList(),
    visibleDates: List<OffsetDateTime> = listOf()
) = CalendarDatePickerState(
    selectedDate,
    availableDates,
    visibleDates
)

fun createReducer(
    timeService: TimeService = mockk(),
    settingsRepository: SettingsRepository = mockk(),
    dispatcherProvider: DispatcherProvider = mockk()
) = CalendarDatePickerReducer(
    timeService,
    settingsRepository,
    dispatcherProvider
)