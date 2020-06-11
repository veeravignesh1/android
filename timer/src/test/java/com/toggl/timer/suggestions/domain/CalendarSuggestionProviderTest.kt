package com.toggl.timer.suggestions.domain

import com.toggl.common.Constants
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.User
import com.toggl.models.validation.ApiToken
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createCalendarEvent
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.stream.Stream

@ExperimentalCoroutinesApi
@DisplayName("The CalendarSuggestionProvider")
class CalendarSuggestionProviderTest : CoroutineTest() {

    private val timeService = mockk<TimeService> { every { now() } returns now }
    private val calendarService = mockk<CalendarService> { every { getAvailableCalendars() } returns emptyList() }
    private val provider = CalendarSuggestionProvider(
        timeService,
        calendarService,
        dispatcherProvider,
        Constants.Suggestions.maxNumberOfCalendarSuggestions
    )

    @Test
    fun `gets events within 1 hour before or after now`() = runBlockingTest {

        val span = Duration.ofHours(1)
        val expectedStartOfRange = now - span
        val expectedEndOfRange = now + span
        val initialState = createInitialState()
        every { calendarService.getCalendarEvents(any(), any(), any()) } returns emptyList()

        provider.getSuggestions(initialState)

        verify {
            calendarService.getCalendarEvents(expectedStartOfRange, expectedEndOfRange, any())
        }
    }

    @Test
    fun `does not include calendar events without a description`() = runBlockingTest {

        val initialState = createInitialState()
        val calendarEvents = listOf(createCalendarEvent(description = ""))
        every { calendarService.getCalendarEvents(any(), any(), any()) } returns calendarEvents

        val suggestions = provider.getSuggestions(initialState)

        suggestions.shouldBeEmpty()
    }

    @Test
    fun `creates the suggestions using the default workspace id of the current user`() = runBlockingTest {

        val user = User(ApiToken.Invalid, 10)
        val initialState = createInitialState(user = user)
        val calendarEvents = listOf("This is valid", "This is also valid").map { createCalendarEvent(description = it) }
        every { calendarService.getCalendarEvents(any(), any(), any()) } returns calendarEvents

        val suggestions = provider.getSuggestions(initialState)

        suggestions.all { it is Suggestion.Calendar && it.workspaceId == user.defaultWorkspaceId }.shouldBeTrue()
    }

    @Test
    fun `returns at most N calendar suggestions`() = runBlockingTest {

        val maxSuggestionNumber = Constants.Suggestions.maxNumberOfCalendarSuggestions

        val user = User(ApiToken.Invalid, 10)
        val initialState = createInitialState(user = user)
        val calendarEvents = listOf("This is valid", "This is also valid").map { createCalendarEvent(description = it) }
        every { calendarService.getCalendarEvents(any(), any(), any()) } returns calendarEvents

        val suggestions = provider.getSuggestions(initialState)

        suggestions shouldHaveSize maxSuggestionNumber
    }

    @ParameterizedTest
    @MethodSource("calendarEvents")
    fun `sorts by the distance between the event start and now`(testData: CalendarTestData) = runBlockingTest {

        every { calendarService.getCalendarEvents(any(), any(), any()) } returns testData.events

        val suggestion = provider.getSuggestions(createInitialState()).filterIsInstance<Suggestion.Calendar>().single()

        suggestion.calendarEvent shouldBe testData.closestEvent
    }

    data class CalendarTestData(val events: List<CalendarEvent>, val closestEvent: CalendarEvent)

    companion object {
        private val now = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        @JvmStatic
        fun calendarEvents(): Stream<CalendarTestData> = Stream.of(
            CalendarTestData(
                listOf(
                    createCalendarEvent(description = "Test", startTime = now.plusMinutes(1)),
                    createCalendarEvent(description = "Test", startTime = now.plusMinutes(2)),
                    createCalendarEvent(description = "Test", startTime = now.plusMinutes(3))
                ), createCalendarEvent(description = "Test", startTime = now.plusMinutes(1))
            ),
            CalendarTestData(
                listOf(
                    createCalendarEvent(description = "Test", startTime = now.plusMinutes(3)),
                    createCalendarEvent(description = "Test", startTime = now.minusMinutes(1)),
                    createCalendarEvent(description = "Test", startTime = now.plusMinutes(2))
                ), createCalendarEvent(description = "Test", startTime = now.minusMinutes(1))
            )
        )
    }
}