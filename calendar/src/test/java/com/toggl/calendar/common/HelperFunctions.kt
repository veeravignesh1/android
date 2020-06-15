package com.toggl.calendar.common

import com.toggl.architecture.DispatcherProvider
import com.toggl.calendar.calendarday.domain.CalendarDayState
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.calendar.calendarday.domain.CalendarDayReducer
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryActionHolder
import com.toggl.environment.services.calendar.Calendar
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import java.time.Duration
import java.time.OffsetDateTime

fun createTimeEntry(
    id: Long,
    description: String = "",
    startTime: OffsetDateTime = OffsetDateTime.now(),
    duration: Duration? = Duration.ofMinutes(2),
    billable: Boolean = false,
    projectId: Long? = null,
    workspaceId: Long = 1,
    taskId: Long? = null,
    tags: List<Long> = emptyList()
) =
    TimeEntry(
        id,
        description,
        startTime,
        duration,
        billable,
        workspaceId,
        projectId,
        taskId,
        false,
        tags
    )

fun createCalendarEvent(
    id: String = "",
    description: String = "",
    startTime: OffsetDateTime = OffsetDateTime.now(),
    duration: Duration = Duration.ofMinutes(2),
    color: String = "#c2c2c2",
    calendarId: String = ""
) = CalendarEvent(
    id,
    startTime,
    duration,
    description,
    color,
    calendarId
)

fun createInitialState(
    timeEntries: List<TimeEntry> = listOf(),
    calendarEvents: List<CalendarEvent> = listOf(),
    projects: List<Project> = listOf(),
    selectedItem: SelectedCalendarItem? = null,
    date: OffsetDateTime = OffsetDateTime.now(),
    calendars: List<Calendar> = listOf()
) = CalendarDayState(
    timeEntries.associateBy { it.id },
    projects.associateBy { it.id },
    calendarEvents.associateBy { it.id },
    selectedItem,
    date,
    calendars
)

fun createCalendarDayReducer(
    calendarService: CalendarService = mockk(),
    timeService: TimeService = mockk(),
    dispatcherProvider: DispatcherProvider
) = CalendarDayReducer(
    calendarService,
    timeService,
    dispatcherProvider
)

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