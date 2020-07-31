package com.toggl.onboarding.login.domain

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
@DisplayName("The GoToSignUpTapped action")
class GoToSignUpTappedActionTests : CoroutineTest() {
    val reducer = createLoginReducer(dispatcherProvider = dispatcherProvider)
    val initialState = emptyLoginState()

    @Test
    fun `sets the backStack to the route (Welcome, SignUp)`() = runBlocking {
        reducer.testReduceState(
            initialState,
            LoginAction.GoToSignUpTapped
        ) {
            it.backStack shouldContainInOrder listOf(Route.Welcome, Route.SignUp)
        }
    }

    @Test
    fun `produces no effects`() = runBlocking {
        reducer.testReduceNoEffects(
            initialState,
            LoginAction.GoToSignUpTapped
        )
    }
}