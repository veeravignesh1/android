package com.toggl.timer.running.domain

import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.common.domain.StartTimeEntryEffect
import com.toggl.timer.common.testReduce
import io.kotlintest.matchers.collections.shouldBeSingleton
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.mockk.mockk

class StartButtonTappedActionTests : FreeSpec({
    val repository = mockk<TimeEntryRepository>()
    val reducer = RunningTimeEntryReducer(repository)
    val editableTimeEntry = EditableTimeEntry.fromSingle(createTimeEntry(1, description = "Test"))
    val initialState = createInitialState(editableTimeEntry = editableTimeEntry)

    "The StartButtonTapped action" - {
        reducer.testReduce(
            initialState = initialState,
            action = RunningTimeEntryAction.StartButtonTapped
        ) { state, effect ->
            "shouldn't change the state" {
                state shouldBe initialState
            }
            "should emit StartTimeEntry effect" {
                effect.shouldBeSingleton()
                effect.single()
                    .shouldBeTypeOf<StartTimeEntryEffect<RunningTimeEntryAction.TimeEntryStarted>>()
            }
        }
    }
})