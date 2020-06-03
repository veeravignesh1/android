package com.toggl.calendar.contextualmenu.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effectOf
import com.toggl.architecture.extensions.effects
import com.toggl.architecture.extensions.noEffect
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.exception.SelectedItemShouldBeAATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldBeACalendarEventException
import com.toggl.calendar.extensions.toEditableTimeEntry
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
            ContextualMenuAction.CloseButtonTapped -> effectOf(ContextualMenuAction.Close)
            ContextualMenuAction.DeleteButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfRunning()

                val idOfEntryToDelete = editableTimeEntry.ids.single()
                delete(idOfEntryToDelete)
            }
            ContextualMenuAction.ContinueButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfRunning()

                val idOfEntryToContinue = editableTimeEntry.ids.first()
                continueTimeEntry(idOfEntryToContinue)
            }
            ContextualMenuAction.StopButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfStopped()

                stop()
            }
            ContextualMenuAction.StartFromEventButtonTapped -> {
                val calendarEvent = state.mapToCalendarEvent()
                val editableTimeEntry = calendarEvent.toEditableTimeEntry(state().defaultWorkspaceId())

                start(editableTimeEntry)
            }
            ContextualMenuAction.CopyAsTimeEntryButtonTapped -> {
                val calendarEvent = state.mapToCalendarEvent()
                val editableTimeEntry = calendarEvent.toEditableTimeEntry(state().defaultWorkspaceId())

                create(editableTimeEntry)
            }
            is ContextualMenuAction.TimeEntryHandling -> noEffect()
            ContextualMenuAction.Close -> TODO()
        }

    private fun stop(): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.StopRunningTimeEntry
        return effects(ContextualMenuAction.TimeEntryHandling(action), ContextualMenuAction.Close)
    }

    private fun delete(id: Long): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.DeleteTimeEntry(id)
        return effects(ContextualMenuAction.TimeEntryHandling(action), ContextualMenuAction.Close)
    }

    private fun continueTimeEntry(id: Long): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.ContinueTimeEntry(id)
        return effects(ContextualMenuAction.TimeEntryHandling(action))
    }

    private fun start(editableTimeEntry: EditableTimeEntry): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.StartTimeEntry(editableTimeEntry.toStartDto(timeService.now()))
        return effects(ContextualMenuAction.TimeEntryHandling(action), ContextualMenuAction.Close)
    }

    private fun create(editableTimeEntry: EditableTimeEntry): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.CreateTimeEntry(editableTimeEntry.toCreateDto())
        return effects(ContextualMenuAction.TimeEntryHandling(action), ContextualMenuAction.Close)
    }

    private fun MutableValue<ContextualMenuState>.mapToCalendarEvent() =
        mapState {
            when (selectedItem) {
                is SelectedCalendarItem.SelectedTimeEntry -> throw SelectedItemShouldBeACalendarEventException()
                is SelectedCalendarItem.SelectedCalendarEvent -> selectedItem.calendarEvent
            }
        }

    private fun MutableValue<ContextualMenuState>.mapToEditableTimeEntry() =
        mapState {
            when (selectedItem) {
                is SelectedCalendarItem.SelectedCalendarEvent -> throw SelectedItemShouldBeAATimeEntryException()
                is SelectedCalendarItem.SelectedTimeEntry -> selectedItem.editableTimeEntry
            }
        }

    private fun ContextualMenuState.defaultWorkspaceId() = 1L
}
