package com.toggl.timer.project.domain

import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.Workspace
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.assertNoEffectsWereReturned
import com.toggl.timer.common.testReduce
import com.toggl.timer.common.testReduceException
import com.toggl.timer.common.testReduceState
import com.toggl.timer.exceptions.EditableProjectShouldNotBeNullException
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The WorkspacePicked action")
internal class WorkspacePickedActionTests : CoroutineTest() {
    private val repository = mockk<ProjectRepository>()
    private val reducer = ProjectReducer(repository, dispatcherProvider)

    @Test
    fun `throws if the editable time entry is null`() = runBlockingTest {
        val initialState = createInitialState(editableProject = null)

        reducer.testReduceException(
            initialState,
            ProjectAction.WorkspacePicked(Workspace(1, "", emptyList())),
            EditableProjectShouldNotBeNullException::class.java
        )
    }

    @Test
    fun `sets the editableProject's workspaceId property`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)
        val workspace = Workspace(10, "The New Workspace", emptyList())

        reducer.testReduceState(
            initialState = initialState,
            action = ProjectAction.WorkspacePicked(workspace)
        ) { state -> state.editableProject?.workspaceId shouldBe workspace.id }
    }

    @Test
    fun `returns no effects`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)
        val workspace = Workspace(10, "The New Workspace", emptyList())

        reducer.testReduce(
            initialState = initialState,
            action = ProjectAction.WorkspacePicked(workspace),
            testCase = ::assertNoEffectsWereReturned
        )
    }
}