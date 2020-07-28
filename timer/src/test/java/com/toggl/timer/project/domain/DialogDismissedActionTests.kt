package com.toggl.timer.project.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.models.domain.EditableProject
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.testReduceEffects

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The DialogDismissed action")
internal class DialogDismissedActionTests : CoroutineTest() {
    private val reducer = createProjectReducer(dispatcherProvider = dispatcherProvider)

    @Test
    fun `returns an effect to close the view`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)

        reducer.testReduceEffects(
            initialState = initialState,
            action = ProjectAction.DialogDismissed
        ) { effects ->
            val closeAction = effects.single().execute() as? ProjectAction.Close
            assertThat(closeAction).isNotNull()
        }
    }
}