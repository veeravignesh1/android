package com.toggl.calendar.contextualmenu.domain

import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.calendar.common.testReduceNoEffects
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
@DisplayName("The DialogDismissed action")
internal class DialogDismissedActionTests {

    private val timeService = mockk<TimeService> { every { now() } returns OffsetDateTime.now() }
    private val reducer = ContextualMenuReducer(timeService)

    @Test
    fun `returns no effects`() = runBlockingTest {
        val timeEntry = EditableTimeEntry.empty(1)
        val initialState = createInitialState(selectedItem = SelectedCalendarItem.SelectedTimeEntry(timeEntry))

        reducer.testReduceNoEffects(initialState, ContextualMenuAction.DialogDismissed)
    }
}