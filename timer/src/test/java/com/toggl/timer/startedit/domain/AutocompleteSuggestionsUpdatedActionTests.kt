package com.toggl.timer.startedit.domain

import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.testReduce
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainInOrder
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The AutocompleteSuggestionsUpdated action")
internal class AutocompleteSuggestionsUpdatedActionTests : CoroutineTest() {
    val repository = mockk<TimeEntryRepository>()
    val initialState = createInitialState()
    val reducer = StartEditReducer(repository, dispatcherProvider)

    @Test
    fun `should update the list of Autocomplete suggestions`() = runBlockingTest {
        val expectedSuggestions = listOf(AutocompleteSuggestion.TimeEntry(createTimeEntry(1)))

        reducer.testReduce(
            initialState,
            action = StartEditAction.AutocompleteSuggestionsUpdated(expectedSuggestions)
        ) { state, _ ->
            state.autocompleteSuggestions.shouldContainInOrder(expectedSuggestions)
        }
    }

    @Test
    fun `shouldn't return any effect`() = runBlockingTest {
        val expectedSuggestions = listOf(AutocompleteSuggestion.TimeEntry(createTimeEntry(1)))

        reducer.testReduce(
            initialState,
            action = StartEditAction.AutocompleteSuggestionsUpdated(expectedSuggestions)
        ) { _, effect ->
            effect.shouldBeEmpty()
        }
    }
}