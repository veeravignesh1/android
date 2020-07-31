package com.toggl.onboarding.login.domain

import com.toggl.architecture.Failure
import com.toggl.architecture.Loadable
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceState
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The set user error action")
class SetUserErrorActionTests : CoroutineTest() {
    private val reducer = createLoginReducer(dispatcherProvider = dispatcherProvider)

    private val throwable = IllegalAccessException()

    @Test
    fun `sets the user error`() = runBlockingTest {
        val initialState = emptyLoginState()

        reducer.testReduceState(initialState, LoginAction.SetUserError(throwable)) { newState ->
            newState shouldBe initialState.copy(
                user = Loadable.Error(Failure(throwable, ""))
            )
        }
    }
}
