package com.toggl.timer.startedit.domain

import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryActionHolder
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.Tag
import com.toggl.timer.common.domain.TimerAction
import java.time.Duration
import java.time.OffsetDateTime

sealed class StartEditAction {
    object BillableTapped : StartEditAction()
    object CloseButtonTapped : StartEditAction()
    object DialogDismissed : StartEditAction()
    object DoneButtonTapped : StartEditAction()
    data class DescriptionEntered(val description: String, val cursorPosition: Int) : StartEditAction()
    object ProjectButtonTapped : StartEditAction()
    object AddProjectChipTapped : StartEditAction()
    object TagButtonTapped : StartEditAction()
    object AddTagChipTapped : StartEditAction()
    data class AutocompleteSuggestionsUpdated(val autocompleteSuggestions: List<AutocompleteSuggestion>) : StartEditAction()
    data class AutocompleteSuggestionTapped(val autocompleteSuggestion: AutocompleteSuggestion) : StartEditAction()
    data class PickerTapped(val pickerMode: DateTimePickMode) : StartEditAction()
    data class DateTimePicked(val dateTime: OffsetDateTime) : StartEditAction()
    data class DurationInputted(val duration: Duration) : StartEditAction()
    object DateTimePickingCancelled : StartEditAction()
    data class WheelChangedStartTime(val startTime: OffsetDateTime) : StartEditAction()
    data class WheelChangedEndTime(val duration: Duration) : StartEditAction()
    object StopButtonTapped : StartEditAction()
    data class TagCreated(val tag: Tag) : StartEditAction()
    data class TimeEntryHandling(override val timeEntryAction: TimeEntryAction) : StartEditAction(), TimeEntryActionHolder

    companion object {
        fun fromTimerAction(timerAction: TimerAction): StartEditAction? =
            if (timerAction !is TimerAction.StartTimeEntry) null
            else timerAction.startEditAction

        fun toTimerAction(startEditAction: StartEditAction): TimerAction =
            TimerAction.StartTimeEntry(
                startEditAction
            )
    }
}

fun StartEditAction.formatForDebug() =
    when (this) {
        StartEditAction.CloseButtonTapped -> "Close button tapped"
        StartEditAction.DialogDismissed -> "Dialog dismissed"
        StartEditAction.DoneButtonTapped -> "Done button tapped"
        StartEditAction.ProjectButtonTapped -> "Project button tapped"
        StartEditAction.AddProjectChipTapped -> "Add Project chip tapped"
        StartEditAction.TagButtonTapped -> "Tag button tapped"
        StartEditAction.AddTagChipTapped -> "Add tag chip tapped"
        is StartEditAction.PickerTapped -> "Picker tapped with mode $pickerMode"
        is StartEditAction.DescriptionEntered -> "Description changed to $description with cursor at position $cursorPosition"
        StartEditAction.BillableTapped -> "Billable toggled in the running time entry"
        is StartEditAction.AutocompleteSuggestionsUpdated -> "AutocompleteSuggestions updated with $autocompleteSuggestions"
        is StartEditAction.AutocompleteSuggestionTapped -> "AutocompleteSuggestion tapped: $autocompleteSuggestion"
        StartEditAction.DateTimePickingCancelled -> "Picker was cancelled"
        is StartEditAction.DateTimePicked -> "date time picked: $dateTime"
        is StartEditAction.DurationInputted -> "Duration manually inputted: $duration"
        is StartEditAction.WheelChangedStartTime -> "Wheel changed start time: $startTime"
        is StartEditAction.WheelChangedEndTime -> "Wheel changed duration: $duration"
        StartEditAction.StopButtonTapped -> "Stop button tapped"
        is StartEditAction.TagCreated -> "Tag created $tag"
        is StartEditAction.TimeEntryHandling -> "Time entry action $timeEntryAction"
    }
