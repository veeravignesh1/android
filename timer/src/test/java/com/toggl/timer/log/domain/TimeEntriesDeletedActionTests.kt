package com.toggl.timer.log.domain

import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.extensions.noEffect
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.FreeCoroutineSpec
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.toMutableValue
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TimeEntriesDeletedActionTests : FreeCoroutineSpec() {
    init {
        val repository = mockk<TimeEntryRepository>()
        val entriesInDatabase = (1L..10L).map { createTimeEntry(it, "test") }
        val reducer = TimeEntriesLogReducer(repository, dispatcherProvider)

        "The TimeEntriesDeleted action updates the state to remove" - {
            "a single time entry from the time entry list" - {
                var state = createInitialState(entriesInDatabase)
                val mutableValue = state.toMutableValue { state = it }

                val timeEntry = entriesInDatabase.first()
                val action = TimeEntriesLogAction.TimeEntryDeleted(timeEntry)
                val effect = reducer.reduce(mutableValue, action)
                effect shouldBe noEffect()
                state.timeEntries[timeEntry.id] shouldBe null
            }

            "multiple time entries from the time entry list" - {
                var state = createInitialState(entriesInDatabase)
                val mutableValue = MutableValue({ state }, { state = it })

                val timeEntriesToRemove = entriesInDatabase.take(3)
                for (timeEntry in timeEntriesToRemove) {
                    val action = TimeEntriesLogAction.TimeEntryDeleted(timeEntry)
                    val effect = reducer.reduce(mutableValue, action)
                    effect shouldBe noEffect()
                }

                timeEntriesToRemove.forEach {
                    state.timeEntries[it.id] shouldBe null
                }
            }

            "nothing deleted time entry wasn't there in the first place" - {
                var state = createInitialState(entriesInDatabase)
                val mutableValue = state.toMutableValue { state = it }

                val nonExistingTimeEntry = createTimeEntry(321, "I don't exist")
                val action = TimeEntriesLogAction.TimeEntryDeleted(nonExistingTimeEntry)
                val effect = reducer.reduce(mutableValue, action)
                effect shouldBe noEffect()
                state.timeEntries shouldBe entriesInDatabase.associateBy { it.id }
            }
        }
    }
}