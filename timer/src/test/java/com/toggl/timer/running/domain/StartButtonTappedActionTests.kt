package com.toggl.timer.running.domain

import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.services.time.TimeService
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.shouldEmitTimeEntryAction
import com.toggl.timer.common.testReduceEffects
import com.toggl.timer.common.testReduceState
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
class StartButtonTappedActionTests : CoroutineTest() {
    val timeService = mockk<TimeService> { every { now() } returns OffsetDateTime.MAX }
    val reducer = RunningTimeEntryReducer(timeService)
    val initialState = createInitialState()

    @Test
    fun `The StartButtonTapped action shouldn't change the state`() = runBlockingTest {
        reducer.testReduceState(
            initialState = initialState,
            action = RunningTimeEntryAction.StartButtonTapped
        ) { state ->
            state shouldBe initialState
        }
    }

    @Test
    fun `The StartButtonTapped action should emit StartTimeEntry effect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialState = initialState,
            action = RunningTimeEntryAction.StartButtonTapped
        ) { effect ->
            effect.shouldBeSingleton()
            effect.first()
                .shouldEmitTimeEntryAction<RunningTimeEntryAction.TimeEntryHandling, TimeEntryAction.StartTimeEntry>()
        }
    }
}
