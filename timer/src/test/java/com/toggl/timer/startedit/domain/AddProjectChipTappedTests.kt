package com.toggl.timer.startedit.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.common.Constants.AutoCompleteSuggestions.projectToken
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.timer.common.testReduceEffects

import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@kotlinx.coroutines.ExperimentalCoroutinesApi
@DisplayName("The AddProjectChipTapped action")
internal class AddProjectChipTappedTests {
    val initialState = createInitialState()
    val reducer = createReducer()

    @Test
    fun `should return an effect to set description to the project token when the description is empty`() = runBlockingTest {
        reducer.testReduceEffects(initialState, StartEditAction.AddProjectChipTapped) { effects ->
            val action = effects.single().execute() as StartEditAction.DescriptionEntered
            assertThat(action.description).isEqualTo(projectToken.toString())
            assertThat(action.cursorPosition).isEqualTo(1)
        }
    }

    @Test
    fun `should return an effect to append the project token to description`() = runBlockingTest {
        val editableWithDescription =
            EditableTimeEntry(workspaceId = 1, description = "asdf")
        val state = initialState.copy(editableTimeEntry = editableWithDescription)
        reducer.testReduceEffects(state, StartEditAction.AddProjectChipTapped) { effects ->
            val action = effects.single().execute() as StartEditAction.DescriptionEntered
            assertThat(action.description).isEqualTo(editableWithDescription.description + " $projectToken")
            assertThat(action.cursorPosition).isEqualTo(editableWithDescription.description.length + 2)
        }
    }
}
