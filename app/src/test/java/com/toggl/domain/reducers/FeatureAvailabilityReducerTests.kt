package com.toggl.domain.reducers

import com.toggl.architecture.core.Reducer
import com.toggl.architecture.core.isOrWraps
import com.toggl.common.CoroutineTest
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.backStackOf
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.domain.loading.LoadingAction
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Workspace
import com.toggl.models.domain.WorkspaceFeature
import com.toggl.onboarding.common.domain.OnboardingAction
import com.toggl.onboarding.login.domain.LoginAction
import com.toggl.timer.common.domain.TimerAction
import com.toggl.timer.startedit.domain.StartEditAction
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The FeatureAvailabilityReducer")
class FeatureAvailabilityReducerTests : CoroutineTest() {

    val allActions: List<AppAction> = listOf(
        AppAction.Loading(LoadingAction.StartLoading),
        AppAction.Timer(TimerAction.StartEditTimeEntry(StartEditAction.BillableTapped)),
        AppAction.Onboarding(OnboardingAction.Login(LoginAction.LoginButtonTapped))
    )
    val state = AppState()
    val mutableValue = state.toMutableValue { }
    val nonPremiumActions = allActions.filterNot(::actionIsPremium)
    val toggleBillableAction = AppAction.Timer(
        TimerAction.StartEditTimeEntry(
            StartEditAction.BillableTapped
        )
    )

    @Test
    fun `for non premium actions forwards the action to the child reducer`() {
        nonPremiumActions.forEach { appAction ->
            val spyReducer = spyk<Reducer<AppState, AppAction>>()
            val featureAvailabilityReducer = FeatureAvailabilityReducer(spyReducer)

            featureAvailabilityReducer.reduce(mutableValue, appAction)

            verify { spyReducer.reduce(mutableValue, appAction) }
        }
    }

    @Nested
    @DisplayName("for the toggle billable action")
    inner class BillableAction {
        @Test
        fun `forwards the action normally if the edited TE is in a premium workspace`() {
            val state = AppState().copy(
                backStack = backStackOf(Route.StartEdit(EditableTimeEntry.empty(1))),
                workspaces = mapOf(1L to Workspace(1, "Auto created workspace", listOf(WorkspaceFeature.Pro)))
            )
            val mutableValue = state.toMutableValue { }
            val spyReducer = spyk<Reducer<AppState, AppAction>>()
            val featureAvailabilityReducer = FeatureAvailabilityReducer(spyReducer)

            featureAvailabilityReducer.reduce(mutableValue, toggleBillableAction)

            verify { spyReducer.reduce(mutableValue, toggleBillableAction) }
        }

        @Test
        fun `does not forward the action when the edited TE is in a non-premium workspace`() {
            val state = AppState().copy(
                backStack = backStackOf(Route.StartEdit(EditableTimeEntry.empty(1))),
                workspaces = mapOf(1L to Workspace(1, "Auto created workspace", listOf()))
            )
            val mutableValue = state.toMutableValue { }
            val spyReducer = spyk<Reducer<AppState, AppAction>>()
            val featureAvailabilityReducer = FeatureAvailabilityReducer(spyReducer)

            featureAvailabilityReducer.reduce(mutableValue, toggleBillableAction)

            verify(exactly = 0) { spyReducer.reduce(mutableValue, toggleBillableAction) }
        }
    }
}

private fun actionIsPremium(appAction: AppAction) =
    appAction.isOrWraps<StartEditAction.BillableTapped>()