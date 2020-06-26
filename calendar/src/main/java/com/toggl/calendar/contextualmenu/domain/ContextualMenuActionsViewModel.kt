package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.ContinueMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.CopyMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.DeleteMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.DiscardMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.EditMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.SaveMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.StartMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.StopMenuActionViewModel

sealed class ContextualMenuActionsViewModel {
    abstract val actions: List<ContextualMenuActionViewModel>

    object StoppedTimeEntryActions : ContextualMenuActionsViewModel() {
        override val actions = listOf(
            DeleteMenuActionViewModel,
            EditMenuActionViewModel,
            SaveMenuActionViewModel,
            ContinueMenuActionViewModel
        )
    }

    object RunningTimeEntryActions : ContextualMenuActionsViewModel() {
        override val actions = listOf(
            DiscardMenuActionViewModel,
            EditMenuActionViewModel,
            SaveMenuActionViewModel,
            StopMenuActionViewModel
        )
    }

    object NewTimeEntryActions : ContextualMenuActionsViewModel() {
        override val actions = listOf(
            DiscardMenuActionViewModel,
            EditMenuActionViewModel,
            SaveMenuActionViewModel
        )
    }

    object CalendarEventActions : ContextualMenuActionsViewModel() {
        override val actions = listOf(
            CopyMenuActionViewModel,
            StartMenuActionViewModel
        )
    }
}

sealed class ContextualMenuActionViewModel(val action: ContextualMenuAction) {
    object DeleteMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.DeleteButtonTapped)
    object EditMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.EditButtonTapped)
    object SaveMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.SaveButtonTapped)
    object ContinueMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.ContinueButtonTapped)

    object DiscardMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.DiscardButtonTapped)
    object StopMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.StopButtonTapped)

    object CopyMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.CopyAsTimeEntryButtonTapped)
    object StartMenuActionViewModel : ContextualMenuActionViewModel(ContextualMenuAction.StartFromEventButtonTapped)
}
