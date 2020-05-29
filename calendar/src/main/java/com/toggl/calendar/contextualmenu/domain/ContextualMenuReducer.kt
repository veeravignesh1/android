package com.toggl.calendar.contextualmenu.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.noEffect
import com.toggl.architecture.extensions.toEffect
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.exception.SelectedItemShouldBeAATimeEntryException
import com.toggl.calendar.exception.SelectedItemShouldNotBeNullException
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.extensions.returnEffect
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.extensions.throwIfNew
import com.toggl.common.feature.timeentry.extensions.throwIfStopped
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContextualMenuReducer @Inject constructor() : Reducer<ContextualMenuState, ContextualMenuAction> {

    override fun reduce(
        state: MutableValue<ContextualMenuState>,
        action: ContextualMenuAction
    ): List<Effect<ContextualMenuAction>> =
        when (action) {
            ContextualMenuAction.DialogDismissed,
            ContextualMenuAction.DiscardButtonTapped,
            ContextualMenuAction.CloseButtonTapped -> state.mutateWithoutEffects { copy(selectedItem = null) }
            ContextualMenuAction.StopButtonTapped -> {
                val editableTimeEntry = state.mapToEditableTimeEntry()
                editableTimeEntry.throwIfNew()
                editableTimeEntry.throwIfStopped()

                state.mutate { copy(selectedItem = null) } returnEffect stop()
            }
            is ContextualMenuAction.TimeEntryHandling -> noEffect()
        }

    private fun stop(): List<Effect<ContextualMenuAction>> {
        val action = TimeEntryAction.StopRunningTimeEntry
        return effect(ContextualMenuAction.TimeEntryHandling(action).toEffect())
    }

    private fun MutableValue<ContextualMenuState>.mapToEditableTimeEntry() =
        mapState {
            when (selectedItem) {
                null -> throw SelectedItemShouldNotBeNullException()
                is SelectedCalendarItem.SelectedCalendarEvent -> throw SelectedItemShouldBeAATimeEntryException()
                is SelectedCalendarItem.SelectedTimeEntry -> selectedItem.editableTimeEntry
            }
        }
}
