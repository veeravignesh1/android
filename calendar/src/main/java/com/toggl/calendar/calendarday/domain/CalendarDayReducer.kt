package com.toggl.calendar.calendarday.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.calendar.common.domain.toSelectedCalendarItem
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.environment.services.calendar.CalendarService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarDayReducer @Inject constructor(private val calendarService: CalendarService, private val dispatcherProvider: DispatcherProvider) : Reducer<CalendarDayState, CalendarDayAction> {

    override fun reduce(
        state: MutableValue<CalendarDayState>,
        action: CalendarDayAction
    ): List<Effect<CalendarDayAction>> =
        when (action) {
            is CalendarDayAction.ItemTapped -> state.mutateWithoutEffects {
                copy(selectedItem = action.calendarItem.toSelectedCalendarItem())
            }
            CalendarDayAction.CalendarViewAppeared -> effect(state().run {
                FetchCalendarEventsEffect(calendarService, date, date, calendars, dispatcherProvider)
            })
            is CalendarDayAction.CalendarEventsFetched -> state.mutateWithoutEffects {
                copy(events = action.calendarEvents.associateBy { it.id })
            }
        }
}
