package com.toggl.calendar.contextualmenu.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.architecture.extensions.toEffect
import com.toggl.calendar.common.domain.toCalendarEvent
import com.toggl.calendar.common.domain.toEditableTimeEntry
import com.toggl.calendar.extensions.toEditableTimeEntry
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.extensions.returnEffect
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.extensions.throwIfNew
import com.toggl.common.feature.timeentry.extensions.throwIfRunning
import com.toggl.common.feature.timeentry.extensions.throwIfStopped
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.repository.extensions.toCreateDto
import com.toggl.repository.extensions.toStartDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContextualMenuReducer @Inject constructor(
    private val timeService: TimeService
) : Reducer<ContextualMenuState, ContextualMenuAction> {

    override fun reduce(
        state: MutableValue<ContextualMenuState>,
        action: ContextualMenuAction
    ): List<Effect<ContextualMenuAction>> =
        when (action) {
            ContextualMenuAction.DialogDismissed,
            ContextualMenuAction.DiscardButtonTapped,
            ContextualMenuAction.CloseButtonTapped -> state.mutateWithoutEffects { copy(selectedItem = null) }
            ContextualMenuAction.DeleteButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfRunning()

                val idOfEntryToDelete = editableTimeEntry.ids.single()
                state.mutate { copy(selectedItem = null) } returnEffect delete(idOfEntryToDelete)
            }
            ContextualMenuAction.ContinueButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfRunning()

                val idOfEntryToContinue = editableTimeEntry.ids.first()
                state.mutate { copy(selectedItem = null) } returnEffect continueTimeEntry(idOfEntryToContinue)
            }
            ContextualMenuAction.StopButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfStopped()

                state.mutate { copy(selectedItem = null) } returnEffect stop()
            }
            ContextualMenuAction.StartFromEventButtonTapped -> {
                val calendarEvent = state.mapToCalendarEvent()
                val editableTimeEntry = calendarEvent.toEditableTimeEntry(state().defaultWorkspaceId())

                state.mutate { copy(selectedItem = null) } returnEffect start(editableTimeEntry)
            }
            ContextualMenuAction.CopyAsTimeEntryButtonTapped -> {
                val calendarEvent = state.mapToCalendarEvent()
                val editableTimeEntry = calendarEvent.toEditableTimeEntry(state().defaultWorkspaceId())

                state.mutate { copy(selectedItem = null) } returnEffect create(editableTimeEntry)
            }
            is ContextualMenuAction.TimeEntryHandling -> noEffect()
        }

    private fun stop(): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.StopRunningTimeEntry
        return effect(ContextualMenuAction.TimeEntryHandling(action).toEffect())
    }

    private fun delete(id: Long): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.DeleteTimeEntry(id)
        return effect(ContextualMenuAction.TimeEntryHandling(action).toEffect())
    }

    private fun continueTimeEntry(id: Long): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.ContinueTimeEntry(id)
        return effect(ContextualMenuAction.TimeEntryHandling(action).toEffect())
    }

    private fun start(editableTimeEntry: EditableTimeEntry): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.StartTimeEntry(editableTimeEntry.toStartDto(timeService.now()))
        return effect(ContextualMenuAction.TimeEntryHandling(action).toEffect())
    }

    private fun create(editableTimeEntry: EditableTimeEntry): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.CreateTimeEntry(editableTimeEntry.toCreateDto())
        return effect(ContextualMenuAction.TimeEntryHandling(action).toEffect())
    }

    private fun MutableValue<ContextualMenuState>.mapToCalendarEvent() =
        mapState { selectedItem.toCalendarEvent() }

    private fun MutableValue<ContextualMenuState>.mapToEditableTimeEntry() =
        mapState { selectedItem.toEditableTimeEntry() }

    private fun ContextualMenuState.defaultWorkspaceId() = 1L
}
