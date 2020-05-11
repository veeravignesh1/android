package com.toggl.timer.startedit.domain

import com.toggl.environment.services.time.TimeService
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.common.testReduceException
import com.toggl.timer.common.testReduceNoEffects
import com.toggl.timer.common.testReduceState
import com.toggl.timer.exceptions.EditableTimeEntryShouldNotBeNullException
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.util.stream.Stream

@ExperimentalCoroutinesApi
@DisplayName("The WheelOffsettedStartTime action")
class WheelOffsettedStartTimeActionTests {
    val initialState = createInitialState()
    private val timeService = mockk<TimeService>()
    val reducer = createReducer(timeService = timeService)

    private fun createEditableTimeEntry(startTime: OffsetDateTime?, duration: Duration?) =
        EditableTimeEntry(workspaceId = 1, startTime = startTime, duration = duration)

    companion object {
        val now: OffsetDateTime = OffsetDateTime.of(2020, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC)

        @JvmStatic
        fun validRunningOrNewTimeEntryTestData(): Stream<RunningOrNewTimeEntryTestData> = Stream.of(
            RunningOrNewTimeEntryTestData(now, null),
            RunningOrNewTimeEntryTestData(null, Duration.ofHours(1)),
            RunningOrNewTimeEntryTestData(null, Duration.ZERO),
            RunningOrNewTimeEntryTestData(null, null)
        )

        @JvmStatic
        fun validStoppedTimeEntryTestData(): Stream<StoppedTimeEntryTestData> = Stream.of(
            StoppedTimeEntryTestData(now, Duration.ofSeconds(30), now),
            StoppedTimeEntryTestData(now, Duration.ofMinutes(30), now.minusMinutes(30)),
            StoppedTimeEntryTestData(now.minusHours(30), Duration.ofHours(30), now)
        )
    }

    init {
        every { timeService.now() } returns now
    }

    @ParameterizedTest
    @MethodSource("validStoppedTimeEntryTestData")
    fun `sets the startTime DateTime to the editableTimeEntry's startTime, without adjusting anything else`(
        testData: StoppedTimeEntryTestData
    ) = runBlockingTest {
        val initialTimeEntry = createEditableTimeEntry(testData.initialStartTime, testData.initialDuration)
        val initialState = initialState.copy(editableTimeEntry = initialTimeEntry)

        reducer.testReduceState(
            initialState,
            action = StartEditAction.WheelOffsettedStartTime(testData.inputtedStartTime)
        ) {
            it shouldBe initialState.copy(
                editableTimeEntry = it.editableTimeEntry!!.copy(
                    startTime = testData.inputtedStartTime
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("validRunningOrNewTimeEntryTestData")
    fun `doesn't change state for running or new editableTimeEntries`(
        testData: RunningOrNewTimeEntryTestData
    ) = runBlockingTest {
        val initialTimeEntry = createEditableTimeEntry(testData.initialStartTime, testData.initialDuration)
        val initialState = initialState.copy(editableTimeEntry = initialTimeEntry)

        reducer.testReduceState(
            initialState,
            action = StartEditAction.WheelOffsettedStartTime(now.minusMinutes(489))
        ) {
            it shouldBe initialState
        }
    }

    @ParameterizedTest
    @MethodSource("validStoppedTimeEntryTestData")
    fun `Does not have any effect`(
        testData: StoppedTimeEntryTestData
    ) = runBlockingTest {
        val initialTimeEntry = createEditableTimeEntry(testData.initialStartTime, testData.initialDuration)
        val initialState = initialState.copy(editableTimeEntry = initialTimeEntry)

        reducer.testReduceNoEffects(
            initialState,
            action = StartEditAction.WheelOffsettedStartTime(testData.inputtedStartTime)
        )
    }

    @Test
    fun `throws an exception when there is no editableTimeEntry`() = runBlockingTest {
        val initialState = initialState.copy(editableTimeEntry = null)

        reducer.testReduceException(
            initialState,
            action = StartEditAction.WheelOffsettedStartTime(now),
            exception = EditableTimeEntryShouldNotBeNullException::class.java
        )
    }

    data class RunningOrNewTimeEntryTestData(
        val initialStartTime: OffsetDateTime?,
        val initialDuration: Duration?
    )

    data class StoppedTimeEntryTestData(
        val initialStartTime: OffsetDateTime,
        val initialDuration: Duration,
        val inputtedStartTime: OffsetDateTime
    )
}