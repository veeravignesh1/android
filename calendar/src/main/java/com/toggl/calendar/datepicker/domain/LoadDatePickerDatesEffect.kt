package com.toggl.calendar.datepicker.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.common.extensions.beginningOfWeek
import com.toggl.common.extensions.toList
import com.toggl.repository.interfaces.SettingsRepository
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime

class LoadDatePickerDatesEffect(
    private val today: OffsetDateTime,
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<CalendarDatePickerAction.DatesLoaded> {
    private val availableDayCount = 14L

    override suspend fun execute(): CalendarDatePickerAction.DatesLoaded? = withContext(dispatcherProvider.io) {
        val userFirstDayOfTheWeek = settingsRepository.loadUserPreferences().firstDayOfTheWeek
        val firstAvailableDate = today.minusDays(availableDayCount - 1)
        val firstShownDate = firstAvailableDate.beginningOfWeek(userFirstDayOfTheWeek)
        val lastShownDate = today.beginningOfWeek(userFirstDayOfTheWeek).plusDays(6)

        CalendarDatePickerAction.DatesLoaded(
            (firstAvailableDate..today).toList(),
            (firstShownDate..lastShownDate).toList()
        )
    }
}