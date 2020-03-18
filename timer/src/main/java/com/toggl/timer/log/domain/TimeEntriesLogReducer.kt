package com.toggl.timer.log.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.SettableValue
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.models.common.SwipeDirection
import com.toggl.models.domain.TimeEntry
import com.toggl.repository.timeentry.TimeEntryRepository
import com.toggl.timer.common.domain.DeleteTimeEntriesEffect
import com.toggl.timer.common.domain.StartTimeEntryEffect
import com.toggl.timer.common.domain.handleTimeEntryCreationStateChanges
import com.toggl.timer.common.domain.handleTimeEntryDeletionStateChanges
import java.lang.IllegalStateException
import javax.inject.Inject

class TimeEntriesLogReducer @Inject constructor(private val repository: TimeEntryRepository)
    : Reducer<TimeEntriesLogState, TimeEntriesLogAction> {

    override fun reduce(
        state: SettableValue<TimeEntriesLogState>,
        action: TimeEntriesLogAction
    ): List<Effect<TimeEntriesLogAction>> =
            when (action) {
                is TimeEntriesLogAction.ContinueButtonTapped -> {
                    val timeEntryToContinue = state.value.timeEntries[action.id]
                        ?: throw IllegalStateException()

                    startTimeEntry(timeEntryToContinue, repository)
                }
                is TimeEntriesLogAction.TimeEntryTapped -> {
                    val tappedTimeEntry = state.value.timeEntries[action.id]
                        ?: throw IllegalStateException()

                    state.value = state.value.copy(editedTimeEntry = tappedTimeEntry)
                    noEffect()
                }

                is TimeEntriesLogAction.TimeEntryGroupTapped -> {
                    val entryToEdit = state.value.timeEntries[action.ids.first()]
                        ?: throw IllegalStateException()
                    state.value = state.value.copy(editedTimeEntry = entryToEdit)
                    noEffect()
                }

                is TimeEntriesLogAction.TimeEntrySwiped -> {
                    val swipedEntry = state.value.timeEntries[action.id]
                        ?: throw IllegalStateException()

                    when (action.direction) {
                        SwipeDirection.Left -> delete(listOf(swipedEntry), repository)
                        SwipeDirection.Right -> startTimeEntry(swipedEntry, repository)
                    }
                }

                is TimeEntriesLogAction.TimeEntryGroupSwiped -> {

                    when (action.direction) {
                        SwipeDirection.Left -> {
                            val timeEntriesToDelete = action.ids
                                .map {
                                    state.value.timeEntries[it] ?: throw IllegalStateException()
                                }
                            delete(timeEntriesToDelete, repository)
                        }
                        SwipeDirection.Right -> {
                            val timeEntryToStart = state.value.timeEntries[action.ids.first()]
                                ?: throw IllegalStateException()
                            startTimeEntry(timeEntryToStart, repository)
                        }
                    }
                }

                is TimeEntriesLogAction.TimeEntryStarted -> {
                    state.value = state.value.copy(
                        timeEntries = handleTimeEntryCreationStateChanges(
                            state.value.timeEntries,
                            action.startedTimeEntry,
                            action.stoppedTimeEntry
                        )
                    )
                    noEffect()
                }

                is TimeEntriesLogAction.TimeEntriesDeleted -> {
                    state.value = state.value.copy(
                        timeEntries = handleTimeEntryDeletionStateChanges(
                            state.value.timeEntries,
                            action.deletedTimeEntries
                        )
                    )
                    noEffect()
                }
            }

    private fun startTimeEntry(timeEntry: TimeEntry, repository: TimeEntryRepository) =
        effect(StartTimeEntryEffect(repository, timeEntry.description) {
            TimeEntriesLogAction.TimeEntryStarted(it.startedTimeEntry, it.stoppedTimeEntry)
        })

    private fun delete(timeEntry: List<TimeEntry>, repository: TimeEntryRepository) =
        effect(DeleteTimeEntriesEffect(repository, timeEntry) {
            TimeEntriesLogAction.TimeEntriesDeleted(it)
        })
}
