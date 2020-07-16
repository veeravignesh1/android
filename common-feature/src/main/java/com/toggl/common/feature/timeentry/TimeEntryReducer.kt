package com.toggl.common.feature.timeentry

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.timeentry.exceptions.TimeEntryDoesNotExistException
import com.toggl.common.feature.timeentry.extensions.replaceTimeEntryWithId
import com.toggl.common.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.TimeEntry
import com.toggl.repository.dto.StartTimeEntryDTO
import com.toggl.repository.interfaces.TimeEntryRepository
import javax.inject.Inject

class TimeEntryReducer @Inject constructor(
    private val repository: TimeEntryRepository,
    private val timeService: TimeService,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<TimeEntryState, TimeEntryAction> {
    override fun reduce(state: MutableValue<TimeEntryState>, action: TimeEntryAction): List<Effect<TimeEntryAction>> {
        return when (action) {
            is TimeEntryAction.StartTimeEntry -> effect(
                StartTimeEntryEffect(
                    action.startTimeEntryDTO,
                    repository,
                    dispatcherProvider
                )
            )
            is TimeEntryAction.ContinueTimeEntry -> {
                val timeEntryToContinue = state().timeEntries[action.id]
                    ?.let(EditableTimeEntry.Companion::fromSingle)
                    ?: throw TimeEntryDoesNotExistException()
                val startEntryDTO = timeEntryToContinue.run {
                    StartTimeEntryDTO(
                        description,
                        timeService.now(),
                        billable,
                        workspaceId,
                        projectId,
                        taskId,
                        tagIds
                    )
                }
                effect(StartTimeEntryEffect(startEntryDTO, repository, dispatcherProvider))
            }
            is TimeEntryAction.DeleteTimeEntry -> {
                val timeEntry = state().timeEntries[action.id] ?: throw TimeEntryDoesNotExistException()
                effect(DeleteTimeEntryEffect(timeEntry, repository, dispatcherProvider))
            }
            is TimeEntryAction.CreateTimeEntry -> effect(
                CreateTimeEntryEffect(
                    action.createTimeEntryDTO,
                    repository,
                    dispatcherProvider
                )
            )
            is TimeEntryAction.StopRunningTimeEntry -> effect(StopTimeEntryEffect(repository, dispatcherProvider))
            is TimeEntryAction.EditTimeEntry -> effect(EditTimeEntryEffect(action.timeEntry, repository, dispatcherProvider))

            is TimeEntryAction.TimeEntryStarted ->
                state.mutateWithoutEffects {
                    copy(
                        timeEntries = handleTimeEntryCreationStateChanges(
                            timeEntries,
                            action.startedTimeEntry,
                            action.stoppedTimeEntry
                        )
                    )
                }
            is TimeEntryAction.TimeEntryDeleted ->
                state.mutateWithoutEffects {
                    copy(
                        timeEntries = handleTimeEntryDeletionStateChanges(
                            timeEntries,
                            action.deletedTimeEntry
                        )
                    )
                }
            is TimeEntryAction.TimeEntryUpdated ->
                state.mutateWithoutEffects {
                    copy(
                        timeEntries = timeEntries.replaceTimeEntryWithId(action.updatedTimeEntry.id, action.updatedTimeEntry)
                    )
                }
        }
    }

    private fun handleTimeEntryCreationStateChanges(
        timeEntries: Map<Long, TimeEntry>,
        startedTimeEntry: TimeEntry,
        stoppedTimeEntry: TimeEntry?
    ): Map<Long, TimeEntry> {

        val newEntries = timeEntries.toMutableMap()
        newEntries[startedTimeEntry.id] = startedTimeEntry
        if (stoppedTimeEntry != null) {
            newEntries[stoppedTimeEntry.id] = stoppedTimeEntry
        }

        return newEntries.toMap()
    }

    private fun handleTimeEntryDeletionStateChanges(
        timeEntries: Map<Long, TimeEntry>,
        deletedTimeEntry: TimeEntry
    ): Map<Long, TimeEntry> {

        val newEntries = timeEntries.toMutableMap()
        newEntries.remove(deletedTimeEntry.id)

        return newEntries.toMap()
    }
}
