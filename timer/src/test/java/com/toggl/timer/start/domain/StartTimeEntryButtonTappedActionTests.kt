package com.toggl.timer.start.domain

import com.toggl.models.domain.Workspace
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.common.domain.StartTimeEntryEffect
import com.toggl.timer.common.testReduce
import com.toggl.timer.log.domain.TimeEntriesLogAction
import io.kotlintest.matchers.collections.shouldBeSingleton
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.mockk.coEvery
import io.mockk.mockk

class StartTimeEntryButtonTappedActionTests : FreeSpec({
    val repository = mockk<TimeEntryRepository>()
    val workspace = mockk<Workspace>()
    val editableTimeEntry = EditableTimeEntry.fromSingle(createTimeEntry(1, description = "Test"))
    val state = StartTimeEntryState(mapOf(), mapOf(1L to workspace), editableTimeEntry)
    val reducer = StartTimeEntryReducer(repository)
    coEvery { workspace.id } returns 1

    "The StartTimeEntryAction action" - {
        reducer.testReduce(
            initialState = state,
            action = StartTimeEntryAction.StartTimeEntryButtonTapped
        ) { state, effect ->
            "should clear editableTimeEntry" {
                state.editableTimeEntry shouldBe EditableTimeEntry.empty(workspace.id)
            }
            "should return StartTimeEntryEffect effect" {
                effect.shouldBeSingleton()
                effect.single().shouldBeTypeOf<StartTimeEntryEffect<TimeEntriesLogAction.TimeEntryStarted>>()
            }
        }
    }
})