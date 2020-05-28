package com.toggl.timer.project.domain

import com.toggl.models.domain.EditableProject
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.assertNoEffectsWereReturned
import com.toggl.timer.common.testReduce
import com.toggl.timer.common.testReduceState
import io.kotlintest.matchers.types.shouldBeNull
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The DialogDismissed action")
internal class DialogDismissedActionTests : CoroutineTest() {
    private val repository = mockk<ProjectRepository>()
    private val reducer = ProjectReducer(repository, dispatcherProvider)

    @Test
    fun `sets the editableProject property to null`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)

        reducer.testReduceState(
            initialState = initialState,
            action = ProjectAction.DialogDismissed
        ) { state -> state.editableProject.shouldBeNull() }
    }

    @Test
    fun `returns no effects`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)

        reducer.testReduce(
            initialState = initialState,
            action = ProjectAction.DialogDismissed,
            testCase = ::assertNoEffectsWereReturned
        )
    }
}