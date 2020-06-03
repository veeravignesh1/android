package com.toggl.timer.common.domain

import arrow.optics.optics
import com.toggl.architecture.core.ActionWrapper
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
    data class StartEditTimeEntry(override val action: StartEditAction) : TimerAction(), ActionWrapper<StartEditAction>
    data class TimeEntriesLog(override val action: TimeEntriesLogAction) : TimerAction(), ActionWrapper<TimeEntriesLogAction>
    data class RunningTimeEntry(override val action: RunningTimeEntryAction) : TimerAction(), ActionWrapper<RunningTimeEntryAction>
    data class Project(override val action: ProjectAction) : TimerAction(), ActionWrapper<ProjectAction>

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
