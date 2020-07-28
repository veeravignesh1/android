package com.toggl.common.feature.timeentry

import com.google.common.truth.Truth.assertThat
import com.toggl.common.feature.common.CoroutineTest
import com.toggl.common.feature.common.createTimeEntry
import com.toggl.common.feature.common.testReduceEffects
import com.toggl.common.feature.common.testReduceState
import com.toggl.repository.interfaces.TimeEntryRepository

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The EditTimeEntryAction")
class EditTimeEntryActionTest : CoroutineTest() {

    private val repository = mockk<TimeEntryRepository>()
    private val reducer = TimeEntryReducer(repository, mockk(), dispatcherProvider)
    private val initialState = TimeEntryState(mapOf())
    private val timeEntry = createTimeEntry(1)

    @Test
    fun `Should return StartTimeEntryEffect`() = runBlockingTest {
        reducer.testReduceEffects(
            initialState,
            TimeEntryAction.EditTimeEntry(timeEntry)
        ) { effects ->
            assertThat(effects).hasSize(1)
            assertThat(effects.first()).isInstanceOf(EditTimeEntryEffect::class.java)
        }
    }

    @Test
    fun `Shouldn't change the state`() = runBlockingTest {
        reducer.testReduceState(
            initialState,
            TimeEntryAction.EditTimeEntry(timeEntry)
        ) { state -> assertThat(state).isEqualTo(initialState) }
    }
}
