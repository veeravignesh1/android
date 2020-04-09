package com.toggl.timer.startedit.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.models.domain.TimeEntry
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.domain.EditTimeEntryEffect
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.common.domain.StartTimeEntryEffect
import com.toggl.timer.common.domain.StopTimeEntryEffect
import com.toggl.timer.common.domain.handleTimeEntryCreationStateChanges
import com.toggl.timer.extensions.replaceTimeEntryWithId
import javax.inject.Inject

class StartEditReducer @Inject constructor(
    private val repository: TimeEntryRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<StartEditState, StartEditAction> {

    override fun reduce(
        state: SettableValue<StartEditState>,
        action: StartEditAction
    ): List<Effect<StartEditAction>> =
        when (action) {
            StartEditAction.StopTimeEntryButtonTapped -> stopTimeEntry(repository)
            StartEditAction.CloseButtonTapped, StartEditAction.DialogDismissed -> {
                state.value = state.value.copy(editableTimeEntry = null)
                noEffect()
            }
            is StartEditAction.DescriptionEntered -> {
                state.value = StartEditState.editableTimeEntry.modify(state.value) {
                    it.copy(description = action.description)
                }
                noEffect()
            }
            is StartEditAction.TimeEntryUpdated -> {
                val newTimeEntries = state.value.timeEntries
                    .replaceTimeEntryWithId(action.id, action.timeEntry)
                state.value = state.value.copy(
                    timeEntries = newTimeEntries,
                    editableTimeEntry = null
                )
                noEffect()
            }
            is StartEditAction.TimeEntryStarted -> {
                state.value = state.value.copy(
                    timeEntries = handleTimeEntryCreationStateChanges(
                        state.value.timeEntries,
                        action.startedTimeEntry,
                        action.stoppedTimeEntry
                    ),
                    editableTimeEntry = null
                )
                noEffect()
            }
            StartEditAction.ToggleBillable -> {
                state.value = StartEditState.editableTimeEntry.modify(state.value) {
                    it.copy(billable = !it.billable)
                }

                noEffect()
            }
            StartEditAction.DoneButtonTapped -> {
                val editableTimeEntry = state.value.editableTimeEntry!!
                state.value = state.value.copy(editableTimeEntry = null)
                if (editableTimeEntry.shouldStart()) {
                    startTimeEntry(editableTimeEntry, repository)
                } else {
                    val timeEntriesToEdit = editableTimeEntry.ids.mapNotNull { state.value.timeEntries[it] }

                    timeEntriesToEdit.map {
                        editTimeEntry(
                            it.copy(description = editableTimeEntry.description, billable = editableTimeEntry.billable),
                            repository,
                            dispatcherProvider
                        )
                    }
                }
            }
        }

    private fun startTimeEntry(editableTimeEntry: EditableTimeEntry, repository: TimeEntryRepository) =
        effect(
            StartTimeEntryEffect(repository, editableTimeEntry, dispatcherProvider) {
                StartEditAction.TimeEntryStarted(it.startedTimeEntry, it.stoppedTimeEntry)
            }
        )

    private fun stopTimeEntry(repository: TimeEntryRepository) =
        effect(
            StopTimeEntryEffect(repository, dispatcherProvider) {
                StartEditAction.TimeEntryUpdated(
                    it.id,
                    it
                )
            }
        )

    private fun editTimeEntry(timeEntry: TimeEntry, repository: TimeEntryRepository, dispatcherProvider: DispatcherProvider) =
        EditTimeEntryEffect(
            repository,
            timeEntry,
            dispatcherProvider
        ) { updatedTimeEntry ->
            StartEditAction.TimeEntryUpdated(updatedTimeEntry.id, updatedTimeEntry)
        }

    private fun EditableTimeEntry.shouldStart() = this.ids.isEmpty()
}
