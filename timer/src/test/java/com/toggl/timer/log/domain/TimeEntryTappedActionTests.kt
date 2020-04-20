package com.toggl.timer.log.domain

import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.FreeCoroutineSpec
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.toMutableValue
import com.toggl.timer.exceptions.TimeEntryDoesNotExistException
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TimeEntryTappedActionTests : FreeCoroutineSpec() {
    init {
        val repository = mockk<TimeEntryRepository>()
        val reducer = TimeEntriesLogReducer(repository, dispatcherProvider)
        val testTe = createTimeEntry(1, "test")

        "The TimeEntryTapped action" - {
            "should thrown when there are no time entries" - {
                "with the matching id" {
                    val initialState = createInitialState(listOf(testTe))
                    var state = initialState
                    val mutableValue = state.toMutableValue { state = it }
                    shouldThrow<TimeEntryDoesNotExistException> {
                        reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryTapped(2))
                    }
                }

                "at all" {
                    val initialState = createInitialState()
                    assertAll(fn = { id: Long ->
                        var state = initialState
                        val mutableValue = state.toMutableValue { state = it }
                        shouldThrow<TimeEntryDoesNotExistException> {
                            reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryTapped(id))
                        }
                    })
                }
            }

            "set the editing time entry property when the time entry exists" {
                val initialState = createInitialState(listOf(testTe))

                var state = initialState
                val mutableValue = state.toMutableValue { state = it }
                reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryTapped(1))
                state.editableTimeEntry!!.ids.single() shouldBe testTe.id
            }
        }
    }
}
