package com.toggl.timer.log.domain

import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.testReduceNoEffects
import com.toggl.timer.common.testReduceState
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ToggleTimeEntryGroupTappedActionTests : CoroutineTest() {

    val reducer = TimeEntriesLogReducer()

    @Test
    fun `The ToggleTimeEntryGroupTappedAction action should add groupId to expandedGroupId if it wasn't there before`() =
        runBlockingTest {
            reducer.testReduceState(
                initialState = createInitialState(expandedGroupIds = setOf(2, 3)),
                action = TimeEntriesLogAction.ToggleTimeEntryGroupTapped(1)
            ) { state ->
                state.expandedGroupIds shouldContain 1
            }
        }

    @Test
    fun `The ToggleTimeEntryGroupTappedAction action should remove groupId from expandedGroupId if it was already there`() =
        runBlockingTest {
            reducer.testReduceState(
                initialState = createInitialState(expandedGroupIds = setOf(1, 2, 3)),
                action = TimeEntriesLogAction.ToggleTimeEntryGroupTapped(1)
            ) { state ->
                state.expandedGroupIds shouldNotContain 1
            }
        }

    @Test
    fun `The ToggleTimeEntryGroupTappedAction action shouldn't return any effect`() = runBlockingTest {
        reducer.testReduceNoEffects(
            initialState = createInitialState(expandedGroupIds = setOf(1, 2, 3)),
            action = TimeEntriesLogAction.ToggleTimeEntryGroupTapped(1)
        )
    }
}