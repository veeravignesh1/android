package com.toggl.timer.log.domain

import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.FreeCoroutineSpec
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.toMutableValue
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TimeEntryGroupTappedActionTests : FreeCoroutineSpec() {
    init {
        val repository = mockk<TimeEntryRepository>()
        val reducer = TimeEntriesLogReducer(repository, dispatcherProvider)
        val testTe = createTimeEntry(1, "test")

        "The TimeEntryGroupTapped action" - {
            "should thrown when there are no time entries" - {
                "with the matching id" {
                    val initialState = createInitialState(listOf(testTe))
                    var state = initialState
                    val mutableValue = state.toMutableValue { state = it }
                    shouldThrow<IllegalStateException> {
                        reducer.reduce(
                            mutableValue,
                            TimeEntriesLogAction.TimeEntryGroupTapped(listOf(2))
                        )
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
                                TimeEntriesLogAction.TimeEntryGroupTapped(listOf(id))
                            )
                        }
                    })
                }
            }

            "set the editing time entry property when the time entry exists" {
                val initialState = createInitialState(listOf(testTe))

                var state = initialState
                val mutableValue = state.toMutableValue { state = it }
                val idsToEdit = listOf(1L, 2L)
                reducer.reduce(mutableValue, TimeEntriesLogAction.TimeEntryGroupTapped(idsToEdit))
                state.editableTimeEntry!!.ids shouldBe idsToEdit
            }
        }
    }
}
