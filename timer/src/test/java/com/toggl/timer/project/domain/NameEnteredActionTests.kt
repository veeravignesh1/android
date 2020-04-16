package com.toggl.timer.project.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.extensions.noEffect
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The NameEntered action")
internal class NameEnteredActionTests {
    val testDispatcher = TestCoroutineDispatcher()
    val dispatcherProvider = DispatcherProvider(testDispatcher, testDispatcher, Dispatchers.Main)
    val reducer = ProjectReducer(mockk(), dispatcherProvider)

    @Test
    fun `shouldn't return any effect`() {
        reducer.reduce(mockk(), ProjectAction.NameEntered("xxx")) shouldBe noEffect()
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