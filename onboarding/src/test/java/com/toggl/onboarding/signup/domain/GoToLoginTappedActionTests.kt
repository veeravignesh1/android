package com.toggl.onboarding.signup.domain

import com.toggl.common.feature.navigation.Route
import com.toggl.onboarding.common.CoroutineTest
import com.toggl.onboarding.common.testReduceNoEffects
import com.toggl.onboarding.common.testReduceState
import io.kotest.matchers.collections.shouldContainInOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The GoToLoginTapped action")
class GoToLoginTappedActionTests : CoroutineTest() {
    val reducer = createSignUpReducer()
    val initialState = emptySignUpState()

    @Test
    fun `sets the backStack to the route (Welcome, Login)`() = runBlocking {
        reducer.testReduceState(
            initialState,
            SignUpAction.GoToLoginTapped
        ) {
            it.backStack shouldContainInOrder listOf(Route.Welcome, Route.Login)
        }
    }

    @Test
    fun `produces no effects`() = runBlocking {
        reducer.testReduceNoEffects(
            initialState,
            SignUpAction.GoToLoginTapped
        )
    }
}