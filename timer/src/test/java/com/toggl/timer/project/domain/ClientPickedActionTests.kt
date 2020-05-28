package com.toggl.timer.project.domain

import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.Client
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
@DisplayName("The ClientPicked action")
internal class ClientPickedActionTests : CoroutineTest() {
    private val repository = mockk<ProjectRepository>()
    private val reducer = ProjectReducer(repository, dispatcherProvider)

    @Test
    fun `throws if the editable time entry is null`() = runBlockingTest {
        val initialState = createInitialState(editableProject = null)

        reducer.testReduceException(
            initialState,
            ProjectAction.ClientPicked(Client(1, "", 1)),
            EditableProjectShouldNotBeNullException::class.java
        )
    }

    @Test
    fun `sets the editableProject's clientId property`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)
        val client = Client(10, "The New Client", 1)

        reducer.testReduceState(
            initialState = initialState,
            action = ProjectAction.ClientPicked(client)
        ) { state -> state.editableProject?.clientId shouldBe client.id }
    }

    @Test
    fun `returns no effects`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)
        val client = Client(10, "The New Client", 1)

        reducer.testReduce(
            initialState = initialState,
            action = ProjectAction.ClientPicked(client),
            testCase = ::assertNoEffectsWereReturned
        )
    }
}