package com.toggl.timer.startedit.domain

import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Workspace
import com.toggl.repository.Repository
import com.toggl.repository.interfaces.StartTimeEntryResult
import com.toggl.timer.common.FreeCoroutineSpec
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.shouldEmitTimeEntryAction
import com.toggl.timer.common.testReduceEffects
import com.toggl.timer.common.toMutableValue
import com.toggl.timer.exceptions.EditableTimeEntryShouldNotBeNullException
import io.kotlintest.matchers.collections.shouldBeSingleton
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
class DoneButtonTappedActionTests : FreeCoroutineSpec() {
    private val repository = mockk<Repository>()
    private val startTimeEntryResult = mockk<StartTimeEntryResult>()
    private val timeEntry = createTimeEntry(1, "old description")
    private val timeEntry2 = createTimeEntry(2, "old description")
    private val workspace = mockk<Workspace> { every { id } returns 1 }
    private val timeService = mockk<TimeService> { every { now() } returns OffsetDateTime.MAX }
    private val editableTimeEntry =
        EditableTimeEntry.fromSingle(createTimeEntry(1, description = "Test"))
    private val reducer = createReducer(repository = repository, timeService = timeService, dispatcherProvider = dispatcherProvider)
    private val state = createInitialState(
        workspaces = listOf(workspace),
        timeEntries = listOf(timeEntry, timeEntry2),
        editableTimeEntry = editableTimeEntry
    )

    init {
        coEvery { workspace.id } returns 1
        every { startTimeEntryResult.startedTimeEntry } returns mockk()
        every { startTimeEntryResult.stoppedTimeEntry } returns mockk()

        "The DoneButtonTapped action" - {
            "should throw when there's no editable entry" {
                var initialState = state.copy(editableTimeEntry = null)
                val mutableValue = initialState.toMutableValue { initialState = it }

                shouldThrow<EditableTimeEntryShouldNotBeNullException> {
                    reducer.reduce(mutableValue, StartEditAction.DoneButtonTapped)
                }
            }

            "should start the TE if the editable has no ids" {
                val initialState = state.copy(editableTimeEntry = editableTimeEntry.copy(ids = listOf()))

                reducer.testReduceEffects(
                    initialState,
                    StartEditAction.DoneButtonTapped
                ) { effects ->
                    effects.shouldBeSingleton()
                    effects.first().shouldEmitTimeEntryAction<StartEditAction.TimeEntryHandling, TimeEntryAction.StartTimeEntry>()
                }
            }

            "should update the TE if the editable has one id" {
                reducer.testReduceEffects(
                    state.copy(),
                    StartEditAction.DoneButtonTapped
                ) { effects ->
                    effects.shouldBeSingleton()
                    effects.first().shouldEmitTimeEntryAction<StartEditAction.TimeEntryHandling, TimeEntryAction.EditTimeEntry>()
                }
            }

            "should update each TE in a group if the editable has several ids" {
                val initialState = state.copy(editableTimeEntry = editableTimeEntry.copy(ids = listOf(1L, 2L)))
                reducer.testReduceEffects(
                    initialState,
                    StartEditAction.DoneButtonTapped
                ) { effects ->
                    effects shouldHaveSize 2
                    effects.forEach { it.shouldEmitTimeEntryAction<StartEditAction.TimeEntryHandling, TimeEntryAction.EditTimeEntry>() }
                }
            }

            "should update no TEs if they're not in state" {
                var initialState = state.copy(editableTimeEntry = editableTimeEntry.copy(ids = listOf(3L, 4L)))
                val mutableValue = initialState.toMutableValue { initialState = it }

                val result = reducer.reduce(mutableValue, StartEditAction.DoneButtonTapped)
                result.forEach { it.execute() }

                result.size shouldBe 0
                coVerify(exactly = 0) {
                    repository.editTimeEntry(any())
                }
            }
        }
    }
}