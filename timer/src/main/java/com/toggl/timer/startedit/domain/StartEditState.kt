package com.toggl.timer.startedit.domain

import arrow.optics.optics
import com.toggl.common.feature.navigation.BackStack
import com.toggl.common.feature.navigation.getEditableTimeEntryIfAny
import com.toggl.common.feature.navigation.updateEditableTimeEntry
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.Client
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.Workspace
import com.toggl.timer.common.domain.TimerState

@optics
data class StartEditState(
    val tags: Map<Long, Tag>,
    val tasks: Map<Long, Task>,
    val clients: Map<Long, Client>,
    val projects: Map<Long, Project>,
    val workspaces: Map<Long, Workspace>,
    val timeEntries: Map<Long, TimeEntry>,
    val backStack: BackStack,
    val editableTimeEntry: EditableTimeEntry,
    val autocompleteSuggestions: List<AutocompleteSuggestion>,
    val dateTimePickMode: DateTimePickMode,
    val temporalInconsistency: TemporalInconsistency,
    val cursorPosition: Int
) {
    companion object {
        fun fromTimerState(timerState: TimerState): StartEditState? {

            val editableTimeEntry = timerState.backStack.getEditableTimeEntryIfAny() ?: return null

            return StartEditState(
                tags = timerState.tags,
                tasks = timerState.tasks,
                clients = timerState.clients,
                projects = timerState.projects,
                workspaces = timerState.workspaces,
                timeEntries = timerState.timeEntries,
                backStack = timerState.backStack,
                editableTimeEntry = editableTimeEntry,
                autocompleteSuggestions = timerState.localState.autocompleteSuggestions,
                dateTimePickMode = timerState.localState.dateTimePickMode,
                temporalInconsistency = timerState.localState.temporalInconsistency,
                cursorPosition = timerState.localState.cursorPosition
            )
        }

        fun toTimerState(timerState: TimerState, startEditState: StartEditState?) =
            startEditState?.let {
                timerState.copy(
                    tags = startEditState.tags,
                    timeEntries = startEditState.timeEntries,
                    backStack = startEditState.backStack.updateEditableTimeEntry(startEditState.editableTimeEntry),
                    localState = timerState.localState.copy(
                        autocompleteSuggestions = startEditState.autocompleteSuggestions,
                        dateTimePickMode = startEditState.dateTimePickMode,
                        temporalInconsistency = startEditState.temporalInconsistency,
                        cursorPosition = startEditState.cursorPosition
                    )
                )
            } ?: timerState
    }
}
