package com.toggl.timer.startedit.domain

import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.common.domain.TimerAction
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

sealed class StartEditAction {
    object BillableTapped : StartEditAction()
    object CloseButtonTapped : StartEditAction()
    object DialogDismissed : StartEditAction()
    object DoneButtonTapped : StartEditAction()
    data class DescriptionEntered(val description: String, val cursorPosition: Int) : StartEditAction()
    object ProjectButtonTapped : StartEditAction()
    object TagButtonTapped : StartEditAction()
    data class TimeEntryUpdated(val id: Long, val timeEntry: TimeEntry) : StartEditAction()
    data class TimeEntryStarted(val startedTimeEntry: TimeEntry, val stoppedTimeEntry: TimeEntry?) : StartEditAction()
    data class AutocompleteSuggestionsUpdated(val autocompleteSuggestions: List<AutocompleteSuggestion>) : StartEditAction()
    data class AutocompleteSuggestionTapped(val autocompleteSuggestion: AutocompleteSuggestion) : StartEditAction()
    data class PickerTapped(val pickerMode: DateTimePickMode) : StartEditAction()
    data class DateTimePicked(val dateTime: OffsetDateTime) : StartEditAction()
    data class DurationInputted(val duration: Duration) : StartEditAction()
    object DateTimePickingCancelled : StartEditAction()
    data class WheelChangedStartTime(val startTime: OffsetDateTime) : StartEditAction()
    data class WheelChangedEndTime(val duration: Duration) : StartEditAction()
    data class WheelOffsettedStartTime(val startTime: OffsetDateTime) : StartEditAction()

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
        StartEditAction.TagButtonTapped -> "Tag button tapped"
        is StartEditAction.PickerTapped -> "Picker tapped with mode $pickerMode"
        is StartEditAction.DescriptionEntered -> "Description changed to $description with cursor at position $cursorPosition"
        is StartEditAction.TimeEntryUpdated -> "Time entry with id $id updated"
        is StartEditAction.TimeEntryStarted -> "Time entry started with id $startedTimeEntry.id"
        StartEditAction.BillableTapped -> "Billable toggled in the running time entry"
        is StartEditAction.AutocompleteSuggestionsUpdated -> "AutocompleteSuggestions updated with $autocompleteSuggestions"
        is StartEditAction.AutocompleteSuggestionTapped -> "AutocompleteSuggestion tapped: $autocompleteSuggestion"
        StartEditAction.DateTimePickingCancelled -> "Picker was cancelled"
        is StartEditAction.DateTimePicked -> "date time picked: $dateTime"
        is StartEditAction.DurationInputted -> "Duration manually inputted: $duration"
        is StartEditAction.WheelChangedStartTime -> "Wheel changed start time: $startTime"
        is StartEditAction.WheelChangedEndTime -> "Wheel changed duration: $duration"
        is StartEditAction.WheelOffsettedStartTime -> "Wheel offsetted to start time: $startTime"
    }
