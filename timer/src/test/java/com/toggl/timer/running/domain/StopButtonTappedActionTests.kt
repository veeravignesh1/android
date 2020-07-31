package com.toggl.timer.running.domain

import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.services.time.TimeService
import com.toggl.models.domain.Workspace
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.shouldEmitTimeEntryAction
import com.toggl.timer.common.testReduceEffects
import com.toggl.timer.common.testReduceState
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class StopButtonTappedActionTests : CoroutineTest() {
    val workspace = mockk<Workspace>()
    val timeService = mockk<TimeService>()
    val reducer = RunningTimeEntryReducer(timeService)
    val initialState = createInitialState()

    @Test
    fun `The StopButtonTapped action shouldn't change the state`() = runBlockingTest {
        coEvery { workspace.id } returns 1
        reducer.testReduceState(
            initialState = initialState,
            action = RunningTimeEntryAction.StopButtonTapped
        ) { state ->
            state shouldBe initialState
        }
    }

    @Test
    fun `The StopButtonTapped action should emit StopTimeEntry effect`() = runBlockingTest {
        coEvery { workspace.id } returns 1
        reducer.testReduceEffects(
            initialState = initialState,
            action = RunningTimeEntryAction.StopButtonTapped
        ) { effect ->
            effect.shouldBeSingleton()
            effect.single()
                .shouldEmitTimeEntryAction<RunningTimeEntryAction.TimeEntryHandling, TimeEntryAction.StopRunningTimeEntry>()
        }
    }
}
