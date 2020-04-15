package com.toggl.timer.running.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.common.domain.StartTimeEntryEffect
import com.toggl.timer.common.domain.StopTimeEntryEffect
import com.toggl.timer.common.domain.handleTimeEntryCreationStateChanges
import com.toggl.timer.extensions.replaceTimeEntryWithId
import com.toggl.timer.extensions.runningTimeEntryOrNull
import javax.inject.Inject

class RunningTimeEntryReducer @Inject constructor(
    private val repository: TimeEntryRepository,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<RunningTimeEntryState, RunningTimeEntryAction> {

    override fun reduce(
        state: SettableValue<RunningTimeEntryState>,
        action: RunningTimeEntryAction
    ): List<Effect<RunningTimeEntryAction>> =
        when (action) {
            RunningTimeEntryAction.StartButtonTapped -> startTimeEntry(EditableTimeEntry.empty(1), repository)
            RunningTimeEntryAction.StopButtonTapped -> stopTimeEntry(repository)
            RunningTimeEntryAction.CardTapped -> {
                val entryToOpen = state.value.timeEntries.runningTimeEntryOrNull()
                    ?.run(EditableTimeEntry.Companion::fromSingle)
                    ?: EditableTimeEntry.empty(state.value.defaultWorkspaceId())

                state.value = state.value.copy(editableTimeEntry = entryToOpen)
                noEffect()
            }
            is RunningTimeEntryAction.TimeEntryUpdated -> {
                val newTimeEntries = state.value.timeEntries
                    .replaceTimeEntryWithId(action.id, action.timeEntry)
                state.value = state.value.copy(timeEntries = newTimeEntries)
                noEffect()
            }
            is RunningTimeEntryAction.TimeEntryStarted -> {
                state.value = state.value.copy(
                    timeEntries = handleTimeEntryCreationStateChanges(
                        state.value.timeEntries,
                        action.startedTimeEntry,
                        action.stoppedTimeEntry
                    )
                )
                noEffect()
            }
        }

    private fun startTimeEntry(editableTimeEntry: EditableTimeEntry, repository: TimeEntryRepository) =
        effect(
            StartTimeEntryEffect(repository, editableTimeEntry, dispatcherProvider) {
                RunningTimeEntryAction.TimeEntryStarted(it.startedTimeEntry, it.stoppedTimeEntry)
            }
        )

    private fun stopTimeEntry(repository: TimeEntryRepository) =
        effect(
            StopTimeEntryEffect(repository, dispatcherProvider) { stoppedTimeEntry ->
                    RunningTimeEntryAction.TimeEntryUpdated(
                        stoppedTimeEntry.id,
                        stoppedTimeEntry
                    )
            }
        )

    private fun RunningTimeEntryState.defaultWorkspaceId() = 1L
}