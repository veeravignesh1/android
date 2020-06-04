package com.toggl.timer.startedit.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.architecture.extensions.effect
import com.toggl.architecture.extensions.effectOf
import com.toggl.architecture.extensions.noEffect
import com.toggl.architecture.extensions.toEffects
import com.toggl.common.Constants.AutoCompleteSuggestions.projectToken
import com.toggl.common.Constants.AutoCompleteSuggestions.tagToken
import com.toggl.common.Constants.TimeEntry.maxDurationInHours
import com.toggl.common.feature.extensions.mutateWithoutEffects
import com.toggl.common.feature.extensions.returnEffect
import com.toggl.common.feature.timeentry.TimeEntryAction.CreateTimeEntry
import com.toggl.common.feature.timeentry.TimeEntryAction.EditTimeEntry
import com.toggl.common.feature.timeentry.TimeEntryAction.StartTimeEntry
import com.toggl.environment.services.time.TimeService
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.repository.Repository
import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.common.feature.timeentry.extensions.isNew
import com.toggl.common.feature.timeentry.extensions.isRepresentingGroup
import com.toggl.common.feature.timeentry.extensions.isRunning
import com.toggl.common.feature.timeentry.extensions.isRunningOrNew
import com.toggl.common.feature.timeentry.extensions.isStopped
import com.toggl.common.feature.timeentry.extensions.wasNotYetPersisted
import com.toggl.repository.exceptions.DurationShouldNotBeNullException
import com.toggl.repository.exceptions.StartTimeShouldNotBeNullException
import com.toggl.repository.extensions.toCreateDto
import com.toggl.repository.extensions.toStartDto
import com.toggl.timer.exceptions.EditableTimeEntryDoesNotHaveADurationSetException
import com.toggl.timer.exceptions.EditableTimeEntryDoesNotHaveAStartTimeSetException
import com.toggl.timer.exceptions.ProjectDoesNotExistException
import com.toggl.timer.exceptions.TagDoesNotExistException
import com.toggl.timer.extensions.absoluteDurationBetween
import com.toggl.timer.startedit.domain.TemporalInconsistency.DurationTooLong
import com.toggl.timer.startedit.domain.TemporalInconsistency.StartTimeAfterCurrentTime
import com.toggl.timer.startedit.domain.TemporalInconsistency.StartTimeAfterStopTime
import com.toggl.timer.startedit.domain.TemporalInconsistency.StopTimeBeforeStartTime
import com.toggl.timer.startedit.util.findTokenAndQueryMatchesForAutocomplete
import java.time.Duration
import java.time.OffsetDateTime
import javax.inject.Inject

class StartEditReducer @Inject constructor(
    private val repository: Repository,
    private val timeService: TimeService,
    private val dispatcherProvider: DispatcherProvider
) : Reducer<StartEditState, StartEditAction> {

    override fun reduce(
        state: MutableValue<StartEditState>,
        action: StartEditAction
    ): List<Effect<StartEditAction>> {
        return when (action) {
            StartEditAction.CloseButtonTapped,
            StartEditAction.DialogDismissed -> effectOf(StartEditAction.Close)
            is StartEditAction.DescriptionEntered ->
                state.mutate {
                    copy(
                        cursorPosition = action.cursorPosition,
                        editableTimeEntry = editableTimeEntry.copy(
                            description = action.description
                        )
                    )
                } returnEffect updateAutocompleteSuggestions(action, state())

            StartEditAction.BillableTapped ->
                state.mutateWithoutEffects {
                    StartEditState.editableTimeEntry.modify(this) {
                        it.copy(billable = !it.billable)
                    }
                }
            StartEditAction.AddProjectChipTapped,
            StartEditAction.ProjectButtonTapped ->
                state.mutateWithoutEffects {
                    StartEditState.editableTimeEntry.modify(this) {
                        val stringBeforeToken = if (it.description.isEmpty()) "" else "${it.description} "
                        it.copy(description = "$stringBeforeToken$projectToken")
                    }
                }
            StartEditAction.AddTagChipTapped,
            StartEditAction.TagButtonTapped ->
                state.mutateWithoutEffects {
                    StartEditState.editableTimeEntry.modify(this) {
                        val stringBeforeToken = if (it.description.isEmpty()) "" else "${it.description} "
                        it.copy(description = "$stringBeforeToken$tagToken")
                    }
                }
            is StartEditAction.PickerTapped ->
                state.mutateWithoutEffects {
                    copy(dateTimePickMode = action.pickerMode, temporalInconsistency = TemporalInconsistency.None)
                }
            StartEditAction.DoneButtonTapped -> {
                val editableTimeEntry = state().editableTimeEntry

                if (editableTimeEntry.wasNotYetPersisted()) {
                    if (editableTimeEntry.isRunning()) {
                        val dto = editableTimeEntry.toStartDto(timeService.now())
                        effectOf(StartEditAction.TimeEntryHandling(StartTimeEntry(dto)))
                    } else {
                        val dto = editableTimeEntry.toCreateDto()
                        effectOf(StartEditAction.TimeEntryHandling(CreateTimeEntry(dto)))
                    }
                } else {
                    val timeEntriesToEdit = editableTimeEntry.ids.mapNotNull { state().timeEntries[it] }
                    timeEntriesToEdit.map {

                        val isGroup = editableTimeEntry.isRepresentingGroup()
                        val startTime = if (isGroup) it.startTime else editableTimeEntry.startTime ?: throw StartTimeShouldNotBeNullException()
                        val duration = if (isGroup) it.duration else editableTimeEntry.duration ?: throw DurationShouldNotBeNullException()

                        StartEditAction.TimeEntryHandling(EditTimeEntry(
                            it.copy(
                                description = editableTimeEntry.description,
                                billable = editableTimeEntry.billable,
                                workspaceId = editableTimeEntry.workspaceId,
                                projectId = editableTimeEntry.projectId,
                                taskId = editableTimeEntry.taskId,
                                tagIds = editableTimeEntry.tagIds,
                                startTime = startTime,
                                duration = duration
                            )
                        ))
                    }.toEffects()
                } + effectOf(StartEditAction.Close)
            }
            is StartEditAction.AutocompleteSuggestionsUpdated ->
                state.mutateWithoutEffects { copy(autocompleteSuggestions = action.autocompleteSuggestions) }
            is StartEditAction.AutocompleteSuggestionTapped -> {
                when (action.autocompleteSuggestion) {
                    is AutocompleteSuggestion.TimeEntry -> state.mutateWithoutEffects {
                        modifyWithTimeEntrySuggestion(action.autocompleteSuggestion)
                    }
                    is AutocompleteSuggestion.Project -> state.mutateWithoutEffects {
                        modifyWithProjectSuggestion(action.autocompleteSuggestion)
                    }
                    is AutocompleteSuggestion.Task -> state.mutateWithoutEffects {
                        modifyWithTaskSuggestion(action.autocompleteSuggestion)
                    }
                    is AutocompleteSuggestion.Tag -> state.mutateWithoutEffects {
                        modifyWithTagSuggestion(action.autocompleteSuggestion)
                    }
                    is AutocompleteSuggestion.CreateProject -> state.mutateWithoutEffects {
                        modifyWithCreateProjectSuggestion(action.autocompleteSuggestion)
                    }
                    is AutocompleteSuggestion.CreateTag -> {
                        val tagCreationSuggestion = action.autocompleteSuggestion
                        state.mutate {
                            modifyWithCreateTagSuggestion()
                        } returnEffect effect(CreateTagEffect(dispatcherProvider, repository, tagCreationSuggestion.name, 1))
                    }
                }
            }
            StartEditAction.DateTimePickingCancelled -> {
                state.mutateWithoutEffects { copy(dateTimePickMode = DateTimePickMode.None) }
            }
            is StartEditAction.DateTimePicked -> {
                val mode = state().dateTimePickMode
                if (mode == DateTimePickMode.None) {
                    noEffect()
                } else {
                    val editableTimeEntry = state().editableTimeEntry
                    val temporalInconsistency = detectTemporalInconsistencies(editableTimeEntry, mode, action.dateTime)
                    if (temporalInconsistency == TemporalInconsistency.None) {
                        state.mutateWithoutEffects {
                            when (mode) {
                                DateTimePickMode.StartTime, DateTimePickMode.StartDate ->
                                    handleStartTimeEdition(editableTimeEntry, action)
                                DateTimePickMode.EndTime, DateTimePickMode.EndDate ->
                                    handleEndTimeEdition(editableTimeEntry, action)
                                else -> this
                            }
                        }
                    } else {
                        state.mutate {
                            copy(dateTimePickMode = DateTimePickMode.None, temporalInconsistency = temporalInconsistency)
                        }
                        effect(ReopenPickerEffect(mode))
                    }
                }
            }
            is StartEditAction.DurationInputted -> {
                val maxDuration = Duration.ofHours(maxDurationInHours)
                if (action.duration < Duration.ZERO || action.duration > maxDuration)
                    return noEffect()

                state.mutateWithoutEffects {
                    val editableTimeEntry = state().editableTimeEntry

                    StartEditState.editableTimeEntry.modify(this) {
                        if (editableTimeEntry.isRunningOrNew()) it.copy(startTime = timeService.now() - action.duration)
                        else it.copy(duration = action.duration)
                    }
                }
            }
            is StartEditAction.WheelChangedStartTime -> {
                val editableTimeEntry = state().editableTimeEntry
                val maxDuration = Duration.ofHours(maxDurationInHours)
                val now = timeService.now()
                val endTime = editableTimeEntry.endTime(now)
                val newDuration = action.startTime.absoluteDurationBetween(endTime)
                if (action.startTime > endTime || newDuration > maxDuration)
                    return noEffect()

                state.mutateWithoutEffects {
                    StartEditState.editableTimeEntry.modify(this) {
                        if (editableTimeEntry.isRunningOrNew())
                            it.copy(startTime = action.startTime)
                        else
                            it.copy(startTime = action.startTime, duration = newDuration)
                    }
                }
            }
            is StartEditAction.WheelChangedEndTime -> {
                val maxDuration = Duration.ofHours(maxDurationInHours)
                if (action.duration < Duration.ZERO || action.duration > maxDuration)
                    return noEffect()

                state.mutateWithoutEffects {
                    val editableTimeEntry = state().editableTimeEntry
                    if (editableTimeEntry.isNew()) throw EditableTimeEntryDoesNotHaveAStartTimeSetException()
                    if (editableTimeEntry.isRunning()) throw EditableTimeEntryDoesNotHaveADurationSetException()

                    StartEditState.editableTimeEntry.modify(this) {
                        it.copy(duration = action.duration)
                    }
                }
            }
            StartEditAction.StopButtonTapped -> {
                val editableTimeEntry = state().editableTimeEntry
                if (editableTimeEntry.isStopped())
                    return noEffect()

                val startTime = editableTimeEntry.startTime
                val now = timeService.now()

                if (startTime == null)
                    return state.mutateWithoutEffects {
                        StartEditState.editableTimeEntry.modify(this) {
                            it.copy(
                                startTime = now,
                                duration = Duration.ZERO
                            )
                        }
                    }

                val maxDuration = Duration.ofHours(maxDurationInHours)
                val durationUntilNow = startTime.absoluteDurationBetween(now)
                if (startTime > now || durationUntilNow > maxDuration)
                    return noEffect()

                state.mutateWithoutEffects {
                    StartEditState.editableTimeEntry.modify(this) {
                        it.copy(duration = durationUntilNow)
                    }
                }
            }
            is StartEditAction.TagCreated -> {
                state.mutateWithoutEffects {
                    copy(
                        tags = tags + (action.tag.id to action.tag),
                        editableTimeEntry = editableTimeEntry.copy(
                            tagIds = editableTimeEntry.tagIds + action.tag.id
                        )
                    )
                }
            }
            is StartEditAction.TimeEntryHandling,
            StartEditAction.Close -> noEffect()
        }
    }

    private fun StartEditState.modifyWithCreateTagSuggestion(): StartEditState =
        StartEditState.editableTimeEntry.modify(this) {
            val (token, currentQuery) = it.description.findTokenAndQueryMatchesForAutocomplete(tagToken, cursorPosition)
            val delimiter = "$token$currentQuery"
            it.copy(description = it.description.substringBeforeLast(delimiter))
        }

    private fun StartEditState.modifyWithProjectSuggestion(autocompleteSuggestion: AutocompleteSuggestion.Project): StartEditState =
        StartEditState.editableTimeEntry.modify(this) {
            val projectSuggestion = autocompleteSuggestion.project
            val (token, currentQuery) = it.description.findTokenAndQueryMatchesForAutocomplete(projectToken, cursorPosition)
            val delimiter = "$token$currentQuery"
            it.copy(
                description = it.description.substringBefore(delimiter),
                projectId = projectSuggestion.id,
                workspaceId = projectSuggestion.workspaceId,
                editableProject = null
            )
        }

    private fun StartEditState.modifyWithTimeEntrySuggestion(autocompleteSuggestion: AutocompleteSuggestion.TimeEntry): StartEditState =
        StartEditState.editableTimeEntry.modify(this) {
            val timeEntrySuggestion = autocompleteSuggestion.timeEntry
            it.copy(
                workspaceId = timeEntrySuggestion.workspaceId,
                description = timeEntrySuggestion.description,
                projectId = timeEntrySuggestion.projectId,
                tagIds = timeEntrySuggestion.tagIds,
                billable = timeEntrySuggestion.billable,
                taskId = timeEntrySuggestion.taskId
            )
        }

    private fun StartEditState.modifyWithTagSuggestion(autocompleteSuggestion: AutocompleteSuggestion.Tag): StartEditState =
        StartEditState.editableTimeEntry.modify(this) {
            val tagSuggestion = autocompleteSuggestion.tag
            if (tags[tagSuggestion.id] == null) throw TagDoesNotExistException()
            val (token, currentQuery) = it.description.findTokenAndQueryMatchesForAutocomplete(tagToken, cursorPosition)
            val delimiter = "$token$currentQuery"
            it.copy(
                description = it.description.substringBeforeLast(delimiter),
                tagIds = it.tagIds + tagSuggestion.id
            )
        }

    private fun StartEditState.modifyWithCreateProjectSuggestion(autocompleteSuggestion: AutocompleteSuggestion.CreateProject): StartEditState =
        StartEditState.editableTimeEntry.modify(this) {
            it.copy(
                editableProject = EditableProject(
                    name = autocompleteSuggestion.name,
                    workspaceId = 1
                )
            )
        }

    private fun StartEditState.modifyWithTaskSuggestion(autocompleteSuggestion: AutocompleteSuggestion.Task): StartEditState =
        StartEditState.editableTimeEntry.modify(this) {
            val taskSuggestion = autocompleteSuggestion.task
            if (this.projects[taskSuggestion.projectId] == null) throw ProjectDoesNotExistException()
            val (token, currentQuery) = it.description.findTokenAndQueryMatchesForAutocomplete(projectToken, cursorPosition)
            val delimiter = "$token$currentQuery"
            it.copy(
                description = it.description.substringBefore(delimiter),
                projectId = taskSuggestion.projectId,
                taskId = taskSuggestion.id,
                workspaceId = taskSuggestion.workspaceId
            )
        }

    private fun StartEditState.handleEndTimeEdition(
        editableTimeEntry: EditableTimeEntry,
        action: StartEditAction.DateTimePicked
    ): StartEditState {
        val startTime = editableTimeEntry.startTime ?: throw EditableTimeEntryDoesNotHaveAStartTimeSetException()
        return copy(
            dateTimePickMode = DateTimePickMode.None,
            temporalInconsistency = TemporalInconsistency.None,
            editableTimeEntry = editableTimeEntry.copy(
                duration = startTime.absoluteDurationBetween(action.dateTime)
            )
        )
    }

    private fun StartEditState.handleStartTimeEdition(
        editableTimeEntry: EditableTimeEntry,
        action: StartEditAction.DateTimePicked
    ): StartEditState {
        val originalDuration = editableTimeEntry.duration
        return if (originalDuration == null) {
            copy(
                dateTimePickMode = DateTimePickMode.None,
                temporalInconsistency = TemporalInconsistency.None,
                editableTimeEntry = editableTimeEntry.copy(
                    startTime = action.dateTime
                )
            )
        } else {
            val originalStartTime = editableTimeEntry.startTime ?: throw EditableTimeEntryDoesNotHaveAStartTimeSetException()
            copy(
                dateTimePickMode = DateTimePickMode.None,
                temporalInconsistency = TemporalInconsistency.None,
                editableTimeEntry = editableTimeEntry.copy(
                    startTime = action.dateTime,
                    duration = action.dateTime.absoluteDurationBetween(originalStartTime + originalDuration)
                )
            )
        }
    }

    private fun detectTemporalInconsistencies(
        editableTimeEntry: EditableTimeEntry,
        mode: DateTimePickMode,
        newDateTime: OffsetDateTime
    ): TemporalInconsistency {
        val maxDuration = Duration.ofHours(999)
        val now = timeService.now()
        val startTime = editableTimeEntry.startTime
        val duration = editableTimeEntry.duration
        val endTime = (startTime ?: now) + (duration ?: Duration.ZERO)

        return when {
            startTime == null -> detectTemporalInconsistenciesOnEmptyTimeEntry(mode, newDateTime, now, maxDuration)
            duration == null -> detectTemporalInconsistenciesOnRunningTimeEntry(mode, newDateTime, now, maxDuration)
            else -> detectTemporalInconsistenciesOnStoppedTimeEntry(mode, startTime, endTime, newDateTime, maxDuration)
        }
    }

    private fun detectTemporalInconsistenciesOnStoppedTimeEntry(
        mode: DateTimePickMode,
        startTime: OffsetDateTime,
        endTime: OffsetDateTime,
        newDateTime: OffsetDateTime,
        maxDuration: Duration
    ): TemporalInconsistency = when {
        mode.targetsStart() && newDateTime > endTime -> StartTimeAfterStopTime
        mode.targetsStart() && endTime.absoluteDurationBetween(newDateTime) > maxDuration -> DurationTooLong
        mode.targetsEnd() && newDateTime < startTime -> StopTimeBeforeStartTime
        mode.targetsEnd() && newDateTime.absoluteDurationBetween(startTime) > maxDuration -> DurationTooLong
        else -> TemporalInconsistency.None
    }

    private fun detectTemporalInconsistenciesOnRunningTimeEntry(
        mode: DateTimePickMode,
        newDateTime: OffsetDateTime,
        now: OffsetDateTime,
        maxDuration: Duration
    ): TemporalInconsistency = when {
        mode.targetsStart() && now.absoluteDurationBetween(newDateTime) > maxDuration -> DurationTooLong
        mode.targetsStart() && newDateTime > now -> StartTimeAfterCurrentTime
        mode.targetsEnd() -> throw EditableTimeEntryDoesNotHaveADurationSetException()
        else -> TemporalInconsistency.None
    }

    private fun detectTemporalInconsistenciesOnEmptyTimeEntry(
        mode: DateTimePickMode,
        newDateTime: OffsetDateTime,
        now: OffsetDateTime,
        maxDuration: Duration
    ): TemporalInconsistency = when {
        mode.targetsStart() && now.absoluteDurationBetween(newDateTime) > maxDuration -> DurationTooLong
        mode.targetsEnd() -> throw EditableTimeEntryDoesNotHaveAStartTimeSetException()
        else -> TemporalInconsistency.None
    }

    private fun updateAutocompleteSuggestions(
        action: StartEditAction.DescriptionEntered,
        state: StartEditState
    ): List<Effect<StartEditAction>> =
        effect(
            UpdateAutocompleteSuggestionsEffect(
                dispatcherProvider,
                action.description,
                action.cursorPosition,
                state.editableTimeEntry.workspaceId,
                state.tags,
                state.tasks,
                state.clients,
                state.projects,
                state.timeEntries
            )
        )

    private fun EditableTimeEntry.endTime(now: OffsetDateTime): OffsetDateTime =
        startTime?.let { startTime ->
            val relativeDuration = duration ?: startTime.absoluteDurationBetween(now)
            startTime + relativeDuration
        } ?: now

    private fun DateTimePickMode.targetsStart() = this == DateTimePickMode.StartDate || this == DateTimePickMode.StartTime
    private fun DateTimePickMode.targetsEnd() = this == DateTimePickMode.EndDate || this == DateTimePickMode.EndTime
}
