package com.toggl.timer.startedit.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.common.Constants.AutoCompleteSuggestions.tagToken
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.timer.common.testReduceEffects

import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@kotlinx.coroutines.ExperimentalCoroutinesApi
@DisplayName("The TagButtonTapped action")
internal class TagButtonTappedActionTests {
    val initialState = createInitialState()
    val reducer = createReducer()

    @Test
    fun `should return an effect to set description to the tag token when the description is empty`() = runBlockingTest {
        reducer.testReduceEffects(initialState, StartEditAction.TagButtonTapped) { effects ->
            val action = effects.single().execute() as StartEditAction.DescriptionEntered
            assertThat(action.description).isEqualTo(tagToken.toString())
            assertThat(action.cursorPosition).isEqualTo(1)
        }
    }

    @Test
    fun `should return an effect to append the tag token to description`() = runBlockingTest {
        val editableWithDescription =
            EditableTimeEntry(workspaceId = 1, description = "asdf")
        val state = initialState.copy(editableTimeEntry = editableWithDescription)
        reducer.testReduceEffects(state, StartEditAction.TagButtonTapped) { effects ->
            val action = effects.single().execute() as StartEditAction.DescriptionEntered
            assertThat(action.description).isEqualTo(editableWithDescription.description + " $tagToken")
            assertThat(action.cursorPosition).isEqualTo(editableWithDescription.description.length + 2)
        }
    }
}
