package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.testReduceNoEffects
import com.toggl.calendar.common.testReduceState
import com.toggl.models.domain.EditableTimeEntry
import io.kotlintest.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The DiscardButtonTapped action")
internal class DiscardButtonTappedActionTests {

    private val reducer = ContextualMenuReducer()

    @Test
    fun `sets the selectedItem to null`() = runBlockingTest {

        val timeEntry = EditableTimeEntry.empty(1)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(timeEntry))

        reducer.testReduceState(initialState, ContextualMenuAction.DiscardButtonTapped) { state ->
            state shouldBe initialState.copy(selectedItem = null)
        }
    }

    @Test
    fun `returns no effects`() = runBlockingTest {
        val timeEntry = EditableTimeEntry.empty(1)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(timeEntry))

        reducer.testReduceNoEffects(initialState, ContextualMenuAction.DiscardButtonTapped)
    }
}