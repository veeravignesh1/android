package com.toggl.calendar.contextualmenu.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effectOf
import com.toggl.architecture.extensions.noEffect
import com.toggl.architecture.extensions.plus
import com.toggl.architecture.extensions.toEffect
import com.toggl.calendar.common.domain.toCalendarEvent
import com.toggl.calendar.common.domain.toEditableTimeEntry
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
                delete(idOfEntryToDelete) + ContextualMenuAction.Close.toEffect()
            }
            ContextualMenuAction.ContinueButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfRunning()

                val idOfEntryToContinue = editableTimeEntry.ids.first()
                continueTimeEntry(idOfEntryToContinue) + ContextualMenuAction.Close.toEffect()
            }
            ContextualMenuAction.StopButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfStopped()

                stop() + ContextualMenuAction.Close.toEffect()
            }
            ContextualMenuAction.StartFromEventButtonTapped -> {
                val calendarEvent = state.mapToCalendarEvent()
                val editableTimeEntry = calendarEvent.toEditableTimeEntry(state().defaultWorkspaceId())

                start(editableTimeEntry) + ContextualMenuAction.Close.toEffect()
            }
            ContextualMenuAction.CopyAsTimeEntryButtonTapped -> {
                val calendarEvent = state.mapToCalendarEvent()
                val editableTimeEntry = calendarEvent.toEditableTimeEntry(state().defaultWorkspaceId())

                create(editableTimeEntry) + ContextualMenuAction.Close.toEffect()
            }
            is ContextualMenuAction.TimeEntryHandling,
            ContextualMenuAction.Close -> noEffect()
        }

    private fun stop(): Effect<ContextualMenuAction> {
        val action = TimeEntryAction.StopRunningTimeEntry
        return ContextualMenuAction.TimeEntryHandling(action).toEffect()
    }

    private fun delete(id: Long): Effect<ContextualMenuAction> {
        val action = TimeEntryAction.DeleteTimeEntry(id)
        return ContextualMenuAction.TimeEntryHandling(action).toEffect()
    }

    private fun continueTimeEntry(id: Long): Effect<ContextualMenuAction> {
        val action = TimeEntryAction.ContinueTimeEntry(id)
        return ContextualMenuAction.TimeEntryHandling(action).toEffect()
    }

    private fun start(editableTimeEntry: EditableTimeEntry): Effect<ContextualMenuAction> {
        val action = TimeEntryAction.StartTimeEntry(editableTimeEntry.toStartDto(timeService.now()))
        return ContextualMenuAction.TimeEntryHandling(action).toEffect()
    }

    private fun create(editableTimeEntry: EditableTimeEntry): Effect<ContextualMenuAction> {
        val action = TimeEntryAction.CreateTimeEntry(editableTimeEntry.toCreateDto())
        return ContextualMenuAction.TimeEntryHandling(action).toEffect()
    }

    private fun MutableValue<ContextualMenuState>.mapToCalendarEvent() =
        mapState { selectedItem.toCalendarEvent() }

    private fun MutableValue<ContextualMenuState>.mapToEditableTimeEntry() =
        mapState { selectedItem.toEditableTimeEntry() }

    private fun ContextualMenuState.defaultWorkspaceId() = 1L
}
