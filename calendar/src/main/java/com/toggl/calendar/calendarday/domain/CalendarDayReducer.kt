package com.toggl.calendar.calendarday.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.domain.toEditableTimeEntry
import com.toggl.calendar.common.domain.toSelectedCalendarItem
import com.toggl.common.Constants.TimeEntry
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.extensions.withoutEffects
import com.toggl.common.feature.timeentry.extensions.endTime
import com.toggl.common.feature.timeentry.extensions.isRunning
import com.toggl.common.feature.timeentry.extensions.throwIfNew
import com.toggl.common.feature.timeentry.extensions.throwIfRunning
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import java.time.Duration
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarDayReducer @Inject constructor(
    private val calendarService: CalendarService,
    private val timeService: TimeService,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<CalendarDayState, CalendarDayAction> {

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
            is CalendarDayAction.TimeEntryDragged -> state.mutateEditableTimeEntry {
                it.throwIfRunning()
                it.copy(startTime = action.startTime)
            }.withoutEffects()
            is CalendarDayAction.StartTimeDragged -> state.mutateEditableTimeEntry { editableTimeEntry ->
                val prevStartTime = editableTimeEntry.startTime!!
                val newStartTime = action.startTime
                val now = timeService.now()
                when {
                    newStartTime.isAfter(editableTimeEntry.endTime(now)) -> editableTimeEntry
                    editableTimeEntry.isRunning() -> editableTimeEntry.copy(startTime = newStartTime)
                    else -> {
                        val durationDiff = Duration.between(prevStartTime, newStartTime)
                        editableTimeEntry.copy(
                            startTime = newStartTime,
                            duration = editableTimeEntry.duration!!.minus(durationDiff)
                        )
                    }
                }
            }.withoutEffects()
            is CalendarDayAction.StopTimeDragged -> state.mutateEditableTimeEntry {
                it.throwIfRunning()
                if (action.stopTime.isBefore(it.startTime)) return@mutateEditableTimeEntry it
                val durationDiff = Duration.between(it.endTime(), action.stopTime)
                it.copy(duration = it.duration!!.plus(durationDiff))
            }.withoutEffects()
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

    private fun MutableValue<CalendarDayState>.mutateEditableTimeEntry(modifyEditableTimeEntry: (EditableTimeEntry) -> EditableTimeEntry) {
        val editableTimeEntry = this().selectedItem.toEditableTimeEntry()
        editableTimeEntry.throwIfNew()
        return mutate {
            CalendarDayState.selectedItem.modify(this) {
                (it as SelectedCalendarItem.SelectedTimeEntry).copy(editableTimeEntry = modifyEditableTimeEntry(editableTimeEntry))
            }
        }
    }

    private fun CalendarDayState.defaultWorkspaceId(): Long = 1L
}
