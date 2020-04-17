package com.toggl.timer.project.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.extensions.noEffect
import com.toggl.timer.common.domain.EditableProject
import com.toggl.timer.common.testReduce
import com.toggl.timer.common.toMutableValue
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
@DisplayName("The NameEntered action")
internal class NameEnteredActionTests {
    val testDispatcher = TestCoroutineDispatcher()
    val dispatcherProvider = DispatcherProvider(testDispatcher, testDispatcher, Dispatchers.Main)
    val reducer = ProjectReducer(mockk(), dispatcherProvider)

    @Test
    fun `should update state with the new name and produce no effect`() = runBlockingTest {
        val initialState = ProjectState(EditableProject.empty(1), emptyMap())
        reducer.testReduce(initialState, ProjectAction.NameEntered("xxxy")) { state, effect ->
            state.editableProject!!.name shouldBe "xxxy"
            effect shouldBe noEffect()
        }
    }

    @Test
    fun `should throw when editableProject is null`() {
        var initialState = ProjectState(null, emptyMap())
        assertThrows<IllegalStateException> {
            reducer.reduce(
                initialState.toMutableValue { initialState = it },
                ProjectAction.NameEntered("xxyy")
            )
        }
    }

    @BeforeEach
    fun beforeTest() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun afterTest() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}