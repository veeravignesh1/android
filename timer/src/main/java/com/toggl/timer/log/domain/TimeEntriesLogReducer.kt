package com.toggl.timer.log.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.architecture.extensions.toEffect
import com.toggl.architecture.extensions.toEffects
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.push
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryAction.DeleteTimeEntry
import com.toggl.common.feature.timeentry.exceptions.TimeEntryDoesNotExistException
import com.toggl.models.common.SwipeDirection
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.extensions.containsExactly
import com.toggl.timer.log.domain.TimeEntriesLogAction.CommitDeletion
import com.toggl.timer.log.domain.TimeEntriesLogAction.ContinueButtonTapped
import com.toggl.timer.log.domain.TimeEntriesLogAction.TimeEntryGroupSwiped
import com.toggl.timer.log.domain.TimeEntriesLogAction.TimeEntryGroupTapped
import com.toggl.timer.log.domain.TimeEntriesLogAction.TimeEntryHandling
import com.toggl.timer.log.domain.TimeEntriesLogAction.TimeEntrySwiped
import com.toggl.timer.log.domain.TimeEntriesLogAction.TimeEntryTapped
import com.toggl.timer.log.domain.TimeEntriesLogAction.ToggleTimeEntryGroupTapped
import com.toggl.timer.log.domain.TimeEntriesLogAction.UndoButtonTapped
import javax.inject.Inject

class TimeEntriesLogReducer @Inject constructor() : Reducer<TimeEntriesLogState, TimeEntriesLogAction> {

    override fun reduce(
        state: MutableValue<TimeEntriesLogState>,
        action: TimeEntriesLogAction
    ): List<Effect<TimeEntriesLogAction>> =
        when (action) {
            is ContinueButtonTapped -> effect(TimeEntryHandling(TimeEntryAction.ContinueTimeEntry(action.id)).toEffect())
            is TimeEntryTapped ->
                state.mutateWithoutEffects {
                    val entryToEdit = timeEntries[action.id]
                        ?.run(EditableTimeEntry.Companion::fromSingle)
                        ?: throw TimeEntryDoesNotExistException()

                    val route = Route.StartEdit(entryToEdit)
                    copy(backStack = backStack.push(route))
                }

            is TimeEntryGroupTapped ->
                state.mutateWithoutEffects {
                    val entryToEdit = EditableTimeEntry.fromGroup(state().getAllTimeEntriesWithIds(action.ids))
                    val route = Route.StartEdit(entryToEdit)
                    copy(backStack = backStack.push(route))
                }

            is TimeEntrySwiped -> {
                val swipedEntry = state().timeEntries[action.id]
                    ?: throw TimeEntryDoesNotExistException()

                when (action.direction) {
                    SwipeDirection.Left ->
                        handleDeletingSwipe(state, listOf(swipedEntry.id))
                    SwipeDirection.Right -> {
                        effect(TimeEntryHandling(TimeEntryAction.ContinueTimeEntry(action.id)).toEffect())
                    }
                }
            }

            is TimeEntryGroupSwiped -> {
                when (action.direction) {
                    SwipeDirection.Left ->
                        handleDeletingSwipe(state, action.ids)
                    SwipeDirection.Right -> {
                        val timeEntryToStart = EditableTimeEntry.fromGroup(state().getAllTimeEntriesWithIds(action.ids))
                        effect(TimeEntryHandling(TimeEntryAction.ContinueTimeEntry(timeEntryToStart.ids.first())).toEffect())
                    }
                }
            }

            is ToggleTimeEntryGroupTapped ->
                state.mutateWithoutEffects {
                    val newUngroupedTimeEntries =
                        if (expandedGroupIds.contains(action.groupId)) expandedGroupIds - action.groupId
                        else expandedGroupIds + action.groupId
                    copy(expandedGroupIds = newUngroupedTimeEntries)
                }
            is CommitDeletion -> {
                val currentState = state()
                val timeEntryIdsToDelete =
                    if (currentState.entriesPendingDeletion.containsExactly(action.ids)) action.ids
                    else listOf()

                if (timeEntryIdsToDelete.none()) {
                    noEffect()
                } else {
                    state.mutate {
                        val updatedTimeEntries = markDeletedTimeEntries(timeEntries, timeEntryIdsToDelete)
                        copy(timeEntries = updatedTimeEntries, entriesPendingDeletion = setOf())
                    }
                    timeEntryIdsToDelete
                        .map { TimeEntryHandling(DeleteTimeEntry(it)) }
                        .toEffects()
                }
            }
            is UndoButtonTapped ->
                state.mutateWithoutEffects { copy(entriesPendingDeletion = emptySet()) }
            is TimeEntryHandling -> noEffect()
        }

    private fun handleDeletingSwipe(
        state: MutableValue<TimeEntriesLogState>,
        entriesToDelete: List<Long>
    ): List<Effect<TimeEntriesLogAction>> {

        val entriesToCommitDeletion = state().entriesPendingDeletion

        state.mutate {
            val updatedEntries = markDeletedTimeEntries(timeEntries, entriesToCommitDeletion)
            copy(timeEntries = updatedEntries, entriesPendingDeletion = entriesToDelete.toSet())
        }

        return prepareDeleteEffects(state, entriesToCommitDeletion) + WaitForUndoEffect(entriesToDelete)
    }

    private fun prepareDeleteEffects(
        state: MutableValue<TimeEntriesLogState>,
        idsToDelete: Collection<Long>
    ): List<Effect<TimeEntriesLogAction>> =
        idsToDelete
            .mapNotNull { state().timeEntries[it] }
            .map { TimeEntryHandling(DeleteTimeEntry(it.id)) }
            .toEffects()

    private fun markDeletedTimeEntries(
        timeEntries: Map<Long, TimeEntry>,
        deletedTimeEntryIds: Collection<Long>
    ): Map<Long, TimeEntry> =
        timeEntries.map {
            if (deletedTimeEntryIds.contains(it.key)) it.key to it.value.copy(isDeleted = true)
            else it.key to it.value
        }.toMap()

    private fun TimeEntriesLogState.getAllTimeEntriesWithIds(ids: List<Long>): Collection<TimeEntry> {
        return if (this.timeEntries.keys.containsAll(ids)) this.timeEntries.filterKeys { it in ids }.values
        else throw TimeEntryDoesNotExistException()
    }
}
