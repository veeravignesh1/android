package com.toggl.settings.common

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryActionHolder
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.UserPreferences
import com.toggl.repository.interfaces.SettingsRepository
import com.toggl.settings.domain.SelectedSetting
import com.toggl.settings.domain.SettingsReducer
import com.toggl.settings.domain.SettingsState
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import java.time.DayOfWeek

fun createSettingsState(
    userPreferences: UserPreferences = createUserPreferences(),
    selectedSetting: SelectedSetting? = null
) = SettingsState(
    userPreferences = userPreferences,
    selectedSetting = selectedSetting
)

fun createUserPreferences(
    isManualModeEnabled: Boolean = false,
    is24HourClock: Boolean = false,
    selectedWorkspaceId: Long = 1,
    dateFormat: DateFormat = DateFormat.DDMMYYYY_dash,
    durationFormat: DurationFormat = DurationFormat.Classic,
    firstDayOfTheWeek: DayOfWeek = DayOfWeek.WEDNESDAY,
    shouldGroupSimilarTimeEntries: Boolean = false
) = UserPreferences(
    isManualModeEnabled = isManualModeEnabled,
    is24HourClock = is24HourClock,
    selectedWorkspaceId = selectedWorkspaceId,
    dateFormat = dateFormat,
    durationFormat = durationFormat,
    firstDayOfTheWeek = firstDayOfTheWeek,
    shouldGroupSimilarTimeEntries = shouldGroupSimilarTimeEntries
)

fun createSettingsReducer(
    settingsRepository: SettingsRepository = mockk(),
    dispatcherProvider: DispatcherProvider
) = SettingsReducer(settingsRepository, dispatcherProvider)

fun <T> T.toMutableValue(setFunction: (T) -> Unit) =
    MutableValue({ this }, setFunction)

suspend fun <State, Action> Reducer<State, Action>.testReduce(
    initialState: State,
    action: Action,
    testCase: suspend (State, List<Effect<Action>>) -> Unit
) {
    var state = initialState
    val mutableValue = state.toMutableValue { state = it }
    val effect = reduce(mutableValue, action)
    testCase(state, effect)
}

@ExperimentalCoroutinesApi
fun <State, Action, EX : Exception> Reducer<State, Action>.testReduceException(
    initialState: State,
    action: Action,
    exception: Class<EX>
) {
    Assertions.assertThrows(exception) {
        runBlockingTest {
            testReduce(initialState, action) { _, _ -> }
        }
    }
}

suspend fun <State, Action> Reducer<State, Action>.testReduceState(
    initialState: State,
    action: Action,
    testCase: suspend (State) -> Unit
) = testReduce(initialState, action) { state, _ -> testCase(state) }

suspend fun <State, Action> Reducer<State, Action>.testReduceEffects(
    initialState: State,
    action: Action,
    testCase: suspend (List<Effect<Action>>) -> Unit
) = testReduce(initialState, action) { _, effects -> testCase(effects) }

suspend fun <State, Action> Reducer<State, Action>.testReduceNoEffects(
    initialState: State,
    action: Action
) = testReduce(initialState, action, ::assertNoEffectsWereReturned)

@Suppress("UNUSED_PARAMETER")
suspend fun <State, Action> assertNoEffectsWereReturned(state: State, effect: List<Effect<Action>>) {
    effect.shouldBeEmpty()
}

suspend inline fun <reified Holder : TimeEntryActionHolder, reified TimeEntryActionType : TimeEntryAction> Effect<Any>.shouldEmitTimeEntryAction(
    additionalTestBlock: (TimeEntryActionType) -> Unit = {}
) {
    this.execute().shouldBeTypeOf<Holder> {
        it.timeEntryAction.shouldBeTypeOf<TimeEntryActionType> { timeEntryAction ->
            additionalTestBlock(timeEntryAction)
        }
    }
}