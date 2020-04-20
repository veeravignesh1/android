package com.toggl.timer.startedit.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.assertNoEffectsWereReturned
import com.toggl.timer.common.testReduce
import com.toggl.timer.common.toMutableValue
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@kotlinx.coroutines.ExperimentalCoroutinesApi
@DisplayName("The ProjectButtonTapped action")
internal class ProjectButtonTappedActionTests {
    val testDispatcher = TestCoroutineDispatcher()
    val dispatcherProvider = DispatcherProvider(testDispatcher, testDispatcher, Dispatchers.Main)
    val repository = mockk<TimeEntryRepository>()
    val initialState = createInitialState()
    val reducer = StartEditReducer(repository, dispatcherProvider)

    @Test
    fun `should throw if editableTimeEntry is null`() {
        assertThrows<IllegalStateException> {
            var state = initialState.copy(editableTimeEntry = null)
            val mutableValue = state.toMutableValue { state = it }
            reducer.reduce(mutableValue, StartEditAction.ProjectButtonTapped)
        }
    }

    @Test
    fun `should append @ to description and return no effects`() = runBlockingTest {
        reducer.testReduce(initialState, StartEditAction.ProjectButtonTapped) { state, effects ->
            state.editableTimeEntry!!.description shouldBe initialState.editableTimeEntry!!.description + " @"
            assertNoEffectsWereReturned(state, effects)
        }
    }
}