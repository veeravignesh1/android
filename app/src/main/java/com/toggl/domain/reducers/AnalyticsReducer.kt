package com.toggl.domain.reducers

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.noEffect
import com.toggl.common.feature.timeentry.extensions.isRepresentingGroup
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.environment.services.analytics.AnalyticsService
import com.toggl.environment.services.analytics.Event
import com.toggl.environment.services.analytics.parameters.EditViewCloseReason
import com.toggl.environment.services.analytics.parameters.EditViewOpenReason.GroupHeader
import com.toggl.environment.services.analytics.parameters.EditViewOpenReason.RunningTimeEntryCard
import com.toggl.environment.services.analytics.parameters.EditViewOpenReason.SingleTimeEntry
import com.toggl.environment.services.analytics.parameters.TimeEntryDeleteOrigin.GroupedLogSwipe
import com.toggl.environment.services.analytics.parameters.TimeEntryDeleteOrigin.LogSwipe
import com.toggl.environment.services.analytics.parameters.TimeEntryStopOrigin.Manual
import com.toggl.models.common.SwipeDirection
import com.toggl.timer.common.domain.TimerAction
import com.toggl.timer.log.domain.TimeEntriesLogAction
import com.toggl.timer.running.domain.RunningTimeEntryAction
import com.toggl.timer.startedit.domain.StartEditAction
import javax.inject.Inject

class AnalyticsReducer @Inject constructor(
    private val analyticsService: AnalyticsService
) : Reducer<AppState, AppAction> {
    override fun reduce(
        state: MutableValue<AppState>,
        action: AppAction
    ): List<Effect<AppAction>> {
        action.toEvent(state)?.run { analyticsService.track(this) }
        return noEffect()
    }

    private fun AppAction.toEvent(state: MutableValue<AppState>): Event? =
        when (this) {
            is AppAction.Timer -> when (timer) {
                is TimerAction.StartEditTimeEntry -> timer.action.toEvent(state)
                is TimerAction.TimeEntriesLog -> timer.action.toEvent()
                is TimerAction.RunningTimeEntry -> timer.action.toEvent()
                else -> null
            }
            else -> null
        }

    private fun StartEditAction.toEvent(state: MutableValue<AppState>): Event? =
        when (this) {
            StartEditAction.CloseButtonTapped,
            StartEditAction.DialogDismissed -> Event.editViewClosed(EditViewCloseReason.Close)
            StartEditAction.DoneButtonTapped ->
                state().editableTimeEntry?.let {
                    Event.editViewClosed(
                        if (it.isRepresentingGroup()) EditViewCloseReason.GroupSave
                        else EditViewCloseReason.Save
                    )
                }
            else -> null
        }

    private fun RunningTimeEntryAction.toEvent(): Event? =
        when (this) {
            RunningTimeEntryAction.StopButtonTapped -> Event.timeEntryStopped(Manual)
            RunningTimeEntryAction.CardTapped -> Event.editViewOpened(RunningTimeEntryCard)
            else -> null
        }

    private fun TimeEntriesLogAction.toEvent(): Event? =
        when {
            this is TimeEntriesLogAction.TimeEntryTapped
                -> Event.editViewOpened(SingleTimeEntry)
            this is TimeEntriesLogAction.TimeEntryGroupTapped
                -> Event.editViewOpened(GroupHeader)
            this is TimeEntriesLogAction.TimeEntrySwiped && this.direction == SwipeDirection.Right
                -> Event.timeEntryDeleted(LogSwipe)
            this is TimeEntriesLogAction.TimeEntryGroupSwiped && this.direction == SwipeDirection.Right
                -> Event.timeEntryDeleted(GroupedLogSwipe)
            this is TimeEntriesLogAction.UndoButtonTapped ->
                Event.undoTapped()
            else -> null
        }
}
