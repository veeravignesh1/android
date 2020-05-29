package com.toggl.calendar.calendarday.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.domain.toSelectedCalendarItem
import com.toggl.common.Constants.TimeEntry
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.models.domain.EditableTimeEntry
import java.time.OffsetDateTime
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
            is CalendarDayAction.EmptyPositionLongPressed -> state.mutateWithoutEffects {
                val workspaceId = this.defaultWorkspaceId()
                copy(selectedItem = createEmptyTimeEntry(workspaceId, action.startTime))
            }
        }

    private fun createEmptyTimeEntry(workspaceId: Long, startTime: OffsetDateTime): SelectedCalendarItem.SelectedTimeEntry {
        return SelectedCalendarItem.SelectedTimeEntry(
            EditableTimeEntry.stopped(
                workspaceId,
                startTime,
                TimeEntry.defaultTimeEntryDuration
            )
        )
    }

    private fun CalendarDayState.defaultWorkspaceId(): Long = 1L
}
