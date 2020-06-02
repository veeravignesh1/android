package com.toggl.timer.common.domain

import arrow.optics.optics
import com.toggl.timer.log.domain.TimeEntriesLogAction
import com.toggl.timer.log.domain.formatForDebug
import com.toggl.timer.project.domain.ProjectAction
import com.toggl.timer.project.domain.formatForDebug
import com.toggl.timer.project.domain.isCloseAction
import com.toggl.timer.running.domain.RunningTimeEntryAction
import com.toggl.timer.running.domain.formatForDebug
import com.toggl.timer.startedit.domain.StartEditAction
import com.toggl.timer.startedit.domain.formatForDebug
import com.toggl.timer.startedit.domain.isCloseAction

@optics
sealed class TimerAction {
    class StartTimeEntry(val startEditAction: StartEditAction) : TimerAction()
    class TimeEntriesLog(val timeEntriesLogAction: TimeEntriesLogAction) : TimerAction()
    class RunningTimeEntry(val runningTimeEntryAction: RunningTimeEntryAction) : TimerAction()
    class Project(val projectAction: ProjectAction) : TimerAction()

    companion object
}

fun TimerAction.isStartEditCloseAction(): Boolean =
    when (this) {
        is TimerAction.TimeEntriesLog,
        is TimerAction.RunningTimeEntry,
        is TimerAction.Project -> false
        is TimerAction.StartTimeEntry -> startEditAction.isCloseAction()
    }

fun TimerAction.isProjectCloseAction(): Boolean =
    when (this) {
        is TimerAction.TimeEntriesLog,
        is TimerAction.RunningTimeEntry,
        is TimerAction.StartTimeEntry -> false
        is TimerAction.Project -> projectAction.isCloseAction()
    }

fun TimerAction.formatForDebug(): String =
    when (this) {
        is TimerAction.StartTimeEntry -> startEditAction.formatForDebug()
        is TimerAction.TimeEntriesLog -> timeEntriesLogAction.formatForDebug()
        is TimerAction.RunningTimeEntry -> runningTimeEntryAction.formatForDebug()
        is TimerAction.Project -> projectAction.formatForDebug()
    }
