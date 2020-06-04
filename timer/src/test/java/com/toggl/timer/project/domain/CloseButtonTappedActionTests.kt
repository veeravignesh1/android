package com.toggl.timer.project.domain

import com.toggl.models.domain.EditableProject
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.testReduceEffects
import io.kotlintest.matchers.types.shouldNotBeNull
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The CloseButtonTapped action")
internal class CloseButtonTappedActionTests : CoroutineTest() {
    private val repository = mockk<ProjectRepository>()
    private val reducer = ProjectReducer(repository, dispatcherProvider)

    @Test
    fun `returns no effects`() = runBlockingTest {
        val editableProject = EditableProject.empty(1)
        val initialState = createInitialState(editableProject = editableProject)

        reducer.testReduceEffects(
            initialState = initialState,
            action = ProjectAction.CloseButtonTapped
        ) { effects ->
            val closeAction = effects.single().execute() as? ProjectAction.Close
            closeAction.shouldNotBeNull()
        }
    }
}