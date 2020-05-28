package com.toggl.timer.running.domain

import arrow.optics.optics
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryActionHolder
import com.toggl.timer.common.domain.TimerAction

@optics
sealed class RunningTimeEntryAction {
    object StartButtonTapped : RunningTimeEntryAction()
    object StopButtonTapped : RunningTimeEntryAction()
    object CardTapped : RunningTimeEntryAction()
    data class TimeEntryHandling(override val timeEntryAction: TimeEntryAction) : RunningTimeEntryAction(), TimeEntryActionHolder

    companion object {
        fun fromTimerAction(timerAction: TimerAction): RunningTimeEntryAction? =
            if (timerAction !is TimerAction.RunningTimeEntry) null
            else timerAction.runningTimeEntryAction

        fun toTimerAction(timeEntriesLogAction: RunningTimeEntryAction): TimerAction =
            TimerAction.RunningTimeEntry(
                timeEntriesLogAction
            )
    }
}

fun RunningTimeEntryAction.formatForDebug() =
    when (this) {
        RunningTimeEntryAction.StartButtonTapped -> "Start time entry button tapped"
        RunningTimeEntryAction.StopButtonTapped -> "Stop time entry button tapped"
        RunningTimeEntryAction.CardTapped -> "Running time entry card tapped"
        is RunningTimeEntryAction.TimeEntryHandling -> "Time entry action $timeEntryAction"
    }