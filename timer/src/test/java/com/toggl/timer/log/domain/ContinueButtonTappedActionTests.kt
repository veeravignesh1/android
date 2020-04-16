package com.toggl.timer.log.domain

import com.toggl.repository.interfaces.StartTimeEntryResult
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.FreeCoroutineSpec
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.toMutableValue
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ContinueButtonTappedActionTests : FreeCoroutineSpec() {
    init {
        val repository = mockk<TimeEntryRepository>()
        val reducer = TimeEntriesLogReducer(repository, dispatcherProvider)
        val testTe = createTimeEntry(1, "test")
        val stoppedTe = createTimeEntry(2, "stopped")
        coEvery { repository.startTimeEntry(1, "test") } returns StartTimeEntryResult(
            testTe,
            stoppedTe
        )

        "The ContinueButtonTapped action" - {
            "should throw when there are no time entries" - {
                "with the matching id" {
                    val initialState = createInitialState(listOf(testTe))
                    var state = initialState
                    val mutableValue = state.toMutableValue { state = it }

                    shouldThrow<IllegalStateException> {
                        reducer.reduce(mutableValue, TimeEntriesLogAction.ContinueButtonTapped(2))
                    }
                }

                "at all" {
                    val initialState = createInitialState()

                    assertAll(fn = { id: Long ->
                        var state = initialState
                        val mutableValue = state.toMutableValue { state = it }
                        shouldThrow<IllegalStateException> {
                            reducer.reduce(
                                mutableValue,
                                TimeEntriesLogAction.ContinueButtonTapped(id)
                            )
                        }
                    })
                }
            }
            "should start a new time entry" {
                val initialState = createInitialState(timeEntries = listOf(testTe))
                var state = initialState
                val mutableValue = state.toMutableValue { state = it }
                val effect =
                    reducer.reduce(mutableValue, TimeEntriesLogAction.ContinueButtonTapped(1))
                val (started, _) = (effect.single().execute() as TimeEntriesLogAction.TimeEntryStarted)
                started shouldBe testTe
            }
            "should stop the previously running time entry" {
                val initialState = createInitialState(timeEntries = listOf(testTe))
                var state = initialState
                val mutableValue = state.toMutableValue { state = it }
                val effect =
                    reducer.reduce(mutableValue, TimeEntriesLogAction.ContinueButtonTapped(1))
                val (_, stopped) = (effect.single().execute() as TimeEntriesLogAction.TimeEntryStarted)
                stopped shouldBe stoppedTe
            }
        }
    }
}