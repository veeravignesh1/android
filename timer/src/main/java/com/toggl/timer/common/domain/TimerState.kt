package com.toggl.timer.common.domain

import arrow.optics.optics
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.Client
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.Workspace
import com.toggl.models.domain.nullableEditableProject
import com.toggl.models.validation.HSVColor
import com.toggl.timer.startedit.domain.DateTimePickMode
import com.toggl.timer.startedit.domain.TemporalInconsistency

@optics
data class TimerState(
    val tags: Map<Long, Tag>,
    val tasks: Map<Long, Task>,
    val clients: Map<Long, Client>,
    val projects: Map<Long, Project>,
    val workspaces: Map<Long, Workspace>,
    val timeEntries: Map<Long, TimeEntry>,
    val editableTimeEntry: EditableTimeEntry?,
    val localState: LocalState
) {
    data class LocalState internal constructor(
        internal val expandedGroupIds: Set<Long>,
        internal val entriesPendingDeletion: Set<Long>,
        internal val autocompleteSuggestions: List<AutocompleteSuggestion>,
        internal val dateTimePickMode: DateTimePickMode,
        internal val temporalInconsistency: TemporalInconsistency,
        internal val cursorPosition: Int,
        internal val customColor: HSVColor
    ) {
        constructor() : this(
            expandedGroupIds = setOf(),
            entriesPendingDeletion = setOf(),
            autocompleteSuggestions = emptyList(),
            dateTimePickMode = DateTimePickMode.None,
            temporalInconsistency = TemporalInconsistency.None,
            cursorPosition = 0,
            customColor = HSVColor.defaultCustomColor
        )
    }

    companion object
}

fun TimerState.setEditableTimeEntryToNull() =
    copy(editableTimeEntry = null)

fun TimerState.setEditableProjectToNull() =
    TimerState.editableTimeEntry.nullableEditableProject.set(this, null)