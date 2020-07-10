package com.toggl.calendar.datepicker.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.environment.services.time.TimeService
import com.toggl.repository.interfaces.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarDatePickerReducer @Inject constructor(
    private val timeService: TimeService,
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<CalendarDatePickerState, CalendarDatePickerAction> {

    override fun reduce(
        state: MutableValue<CalendarDatePickerState>,
        action: CalendarDatePickerAction
    ): List<Effect<CalendarDatePickerAction>> =
        when (action) {
            is CalendarDatePickerAction.OnViewAppeared -> effect(
                LoadDatePickerDatesEffect(
                    timeService.now(),
                    settingsRepository,
                    dispatcherProvider
                )
            )
            is CalendarDatePickerAction.DatesLoaded -> state.mutateWithoutEffects {
                copy(availableDates = action.availableDates, visibleDates = visibleDates)
            }
        }
}
