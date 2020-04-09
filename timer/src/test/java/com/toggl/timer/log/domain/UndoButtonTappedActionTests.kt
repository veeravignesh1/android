package com.toggl.timer.log.domain

import com.toggl.architecture.extensions.noEffect
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.FreeCoroutineSpec
import com.toggl.timer.common.toSettableValue
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UndoButtonTappedActionTests : FreeCoroutineSpec() {
    init {
        val repository = mockk<TimeEntryRepository>()
        val reducer = TimeEntriesLogReducer(repository, dispatcherProvider)

        "The UndoButtonTapped action" - {
            "clears entriesPendingDeletion if there are any" - {
                var initialState = createInitialState(entriesPendingDeletion = setOf(1, 5, 1337))
                val settableValue = initialState.toSettableValue { initialState = it }

                val effect = reducer.reduce(settableValue, TimeEntriesLogAction.UndoButtonTapped)

                effect shouldBe noEffect()
                initialState.entriesPendingDeletion shouldBe emptySet()
            }
            "keeps entriesPendingDeletion empty if they are empty" - {
                var initialState = createInitialState(entriesPendingDeletion = setOf())
                val settableValue = initialState.toSettableValue { initialState = it }

                val effect = reducer.reduce(settableValue, TimeEntriesLogAction.UndoButtonTapped)

                effect shouldBe noEffect()
                initialState.entriesPendingDeletion shouldBe emptySet()
            }
        }
    }
}