package com.toggl.timer.log.domain

import com.toggl.architecture.extensions.noEffect
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.toMutableValue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class UndoButtonTappedActionTests : CoroutineTest() {

    val reducer = TimeEntriesLogReducer()

    @Test
    fun `The UndoButtonTapped action clears entriesPendingDeletion if there are any`() = runBlockingTest {
        var initialState = createInitialState(entriesPendingDeletion = setOf(1, 5, 1337))
        val mutableValue = initialState.toMutableValue { initialState = it }

        val effect = reducer.reduce(mutableValue, TimeEntriesLogAction.UndoButtonTapped)

        effect shouldBe noEffect()
        initialState.entriesPendingDeletion shouldBe emptySet()
    }

    @Test
    fun `The UndoButtonTapped action keeps entriesPendingDeletion empty if they are empty`() = runBlockingTest {

        var initialState = createInitialState(entriesPendingDeletion = setOf())
        val mutableValue = initialState.toMutableValue { initialState = it }

        val effect = reducer.reduce(mutableValue, TimeEntriesLogAction.UndoButtonTapped)

        effect shouldBe noEffect()
        initialState.entriesPendingDeletion shouldBe emptySet()
    }
}