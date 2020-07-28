package com.toggl.calendar.calendarday.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.calendar.common.createCalendarEvent
import com.toggl.calendar.common.createTimeEntry
import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.calendar.common.domain.description
import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.common.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.contracts.ExperimentalContracts

@ExperimentalCoroutinesApi
@DisplayName("The calendarItemsSelector")
internal class CalendarItemsSelectorTest {

    private val timeService: TimeService = mockk { every { now() } returns OffsetDateTime.now() }
    private val calendarLayoutCalculator = CalendarLayoutCalculator(timeService)
    private val selector = CalendarItemsSelector(calendarLayoutCalculator)

    @Nested
    @DisplayName("when selecting time entries only")
    inner class TimeEntriesOnly {

        @Test
        fun `selects only time entries from the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedTimeEntries = (1..10L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedTimeEntries = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                (expectedTimeEntries + notExpectedTimeEntries),
                emptyList(),
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedTimeEntries.size)

            assertAll(selectedTimeEntries.stream().map {
                {
                    assertThat(it).isInstanceOf(CalendarItem.TimeEntry::class.java)
                    assertThat((it as CalendarItem.TimeEntry).description).isEqualTo("Expected")
                }
            })
        }

        @Test
        fun `returns an empty list when there are no time entries on the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val notExpectedTimeEntries = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                notExpectedTimeEntries,
                emptyList(),
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).isEmpty()
        }

        @Test
        fun `selects only time entries that start and end on the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedTimeEntries = (1..10L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedTimeEntriesThatStartTheDayBefore = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.minusHours(1),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }
            val notExpectedTimeEntriesThatEndTheDayAfter = (21..30L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(23),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                (expectedTimeEntries + notExpectedTimeEntriesThatStartTheDayBefore + notExpectedTimeEntriesThatEndTheDayAfter),
                emptyList(),
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedTimeEntries.size)
            // assertThat(selectedTimeEntries).allMatch { it is CalendarItem.TimeEntry && it.timeEntry.description == "Expected" }
        }
    }

    @Nested
    @DisplayName("when selecting calendar events only")
    inner class CalendarEventsOnly {
        @Test
        fun `selects only events from the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedEvents = (1..10L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedEvents = (11..20L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                emptyList(),
                expectedEvents + notExpectedEvents,
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedEvents.size)
            // assertThat(selectedTimeEntries).allMatch { it is CalendarItem.CalendarEvent && it.calendarEvent.description == "Expected" }
        }

        @Test
        fun `returns an empty list when there are no events on the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val notExpectedEvents = (11..20L).map {
                createCalendarEvent(
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                calendarEvents = notExpectedEvents,
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).isEmpty()
        }

        @Test
        fun `selects only events that start and end on the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedEvents = (1..10L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedEventsThatStartTheDayBefore = (11..20L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.minusHours(1),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }
            val notExpectedEventsThatEndTheDayAfter = (21..30L).map {
                createCalendarEvent(
                    startTime = date.plusHours(23),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                calendarEvents = expectedEvents + notExpectedEventsThatStartTheDayBefore + notExpectedEventsThatEndTheDayAfter,
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedEvents.size)
            // assertThat(selectedTimeEntries).allMatch { it is CalendarItem.CalendarEvent && it.calendarEvent.description == "Expected" }
        }
    }

    @Nested
    @DisplayName("when selecting time entries & calendar events")
    inner class MixAndMatch {
        @Test
        fun `selects only time entries & events from the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedTimeEntries = (1..10L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val expectedEvents = (1..10L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedTimeEntries = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }
            val notExpectedEvents = (11..20L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                (expectedTimeEntries + notExpectedTimeEntries),
                expectedEvents + notExpectedEvents,
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedTimeEntries.size + expectedEvents.size)
            // assertThat(selectedTimeEntries).allMatch {
            //     when (it) {
            //         is CalendarItem.TimeEntry -> it.timeEntry.description == "Expected"
            //         is CalendarItem.CalendarEvent -> it.calendarEvent.description == "Expected"
            //         is CalendarItem.SelectedItem -> false
            //     }
            // }
        }

        @Test
        fun `returns an empty list when there are no time entries or events on the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val notExpectedTimeEntries = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }
            val notExpectedEvents = (11..20L).map {
                createCalendarEvent(
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                notExpectedTimeEntries,
                notExpectedEvents,
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).isEmpty()
        }

        @Test
        fun `selects only time entries & events that start and end on the specified date`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedTimeEntries = (1..10L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val expectedEvents = (1..10L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedTimeEntriesThatStartTheDayBefore = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.minusHours(1),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }
            val notExpectedEventsThatStartTheDayBefore = (11..20L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.minusHours(1),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }
            val notExpectedTimeEntriesThatEndTheDayAfter = (21..30L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(23),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }
            val notExpectedEventsThatEndTheDayAfter = (21..30L).map {
                createCalendarEvent(
                    startTime = date.plusHours(23),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                (expectedTimeEntries + notExpectedTimeEntriesThatStartTheDayBefore + notExpectedTimeEntriesThatEndTheDayAfter),
                expectedEvents + notExpectedEventsThatEndTheDayAfter + notExpectedEventsThatStartTheDayBefore,
                date = date
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedTimeEntries.size + expectedEvents.size)
            // assertThat(selectedTimeEntries).allMatch {
            //     when (it) {
            //         is CalendarItem.TimeEntry -> it.timeEntry.description == "Expected"
            //         is CalendarItem.CalendarEvent -> it.calendarEvent.description == "Expected"
            //         is CalendarItem.SelectedItem -> false
            //     }
            // }
        }
    }

    @ExperimentalContracts
    @Nested
    @DisplayName("when there is a time entry selectedItem set in the state")
    inner class TimeEntrySelectedItemCases {
        @Test
        fun `returns the selected time entries with the replaced editable time entry if its not new`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedTimeEntries = (1..10L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedTimeEntriesThatStartTheDayBefore = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.minusHours(1),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }
            val notExpectedTimeEntriesThatEndTheDayAfter = (21..30L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(23),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }

            val editableTimeEntry = EditableTimeEntry.fromSingle(expectedTimeEntries[0])
                .copy(
                    duration = Duration.ofMinutes(20)
                )
            val state = createInitialState(
                (expectedTimeEntries + notExpectedTimeEntriesThatStartTheDayBefore + notExpectedTimeEntriesThatEndTheDayAfter),
                emptyList(),
                date = date,
                selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry)
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedTimeEntries.size)
            // assertThat(selectedTimeEntries).allMatch {
            //     it is CalendarItem.TimeEntry && it.timeEntry.description == "Expected" ||
            //         it is CalendarItem.SelectedItem && it.description == "Expected"
            // }
            // val condition = Condition<CalendarItem>(
            //     Predicate {
            //         it is CalendarItem.SelectedItem &&
            //             it.duration == Duration.ofMinutes(20) &&
            //             it.selectedCalendarItem.toEditableTimeEntry().ids.first() == 1L
            //     },
            //     "TimeEntry duration was updated"
            // )
            // assertThat(selectedTimeEntries).areExactly(1, condition)
        }

        @Test
        fun `returns the selected time entries plus the editable time if it is new`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedTimeEntries = (1..10L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedTimeEntriesThatStartTheDayBefore = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.minusHours(1),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }
            val notExpectedTimeEntriesThatEndTheDayAfter = (21..30L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(23),
                    duration = Duration.ofHours(2),
                    description = "Unexpected"
                )
            }

            val editableTimeEntry = EditableTimeEntry.stopped(
                1,
                startTime = date.plusHours(1),
                duration = Duration.ofMinutes(30)
            ).copy(
                description = "New Time Entry"
            )

            val state = createInitialState(
                (expectedTimeEntries + notExpectedTimeEntriesThatStartTheDayBefore + notExpectedTimeEntriesThatEndTheDayAfter),
                emptyList(),
                date = date,
                selectedItem = SelectedCalendarItem.SelectedTimeEntry(editableTimeEntry)
            )
            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedTimeEntries.size + 1)
            // val otherExistingSelectedTimeEntriesMatch = Condition<CalendarItem>(
            //     Predicate { it is CalendarItem.TimeEntry && it.timeEntry.description == "Expected" },
            //     "Unmodified time entries are still there"
            // )
            // val newTimeEntryMatches = Condition<CalendarItem>(
            //     Predicate {
            //         it is CalendarItem.SelectedItem &&
            //             it.selectedCalendarItem is SelectedCalendarItem.SelectedTimeEntry &&
            //             it.selectedCalendarItem.toEditableTimeEntry().ids.isEmpty() &&
            //             it.duration == Duration.ofMinutes(30)
            //     },
            //     "New timeEntry was added to the list"
            // )
            // assertThat(selectedTimeEntries).areExactly(10, otherExistingSelectedTimeEntriesMatch)
            // assertThat(selectedTimeEntries).areExactly(1, newTimeEntryMatches)
        }
    }

    @Nested
    @DisplayName("when there is a calendar event selectedItem in the state")
    inner class CalendarEventSelectedItemCase {
        @Test
        fun `returns the list without further changes besides layout calculation`() = runBlockingTest {
            val date = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            val expectedTimeEntries = (1..10L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val expectedEvents = (1..10L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusHours(1),
                    duration = Duration.ofMinutes(10),
                    description = "Expected"
                )
            }
            val notExpectedTimeEntries = (11..20L).map {
                createTimeEntry(
                    it,
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }
            val notExpectedEvents = (11..20L).map {
                createCalendarEvent(
                    id = it.toString(),
                    startTime = date.plusDays(if (it % 2 == 0L) 1 else -1),
                    duration = Duration.ofMinutes(10),
                    description = "Unexpected"
                )
            }

            val state = createInitialState(
                (expectedTimeEntries + notExpectedTimeEntries),
                expectedEvents + notExpectedEvents,
                date = date,
                selectedItem = SelectedCalendarItem.SelectedCalendarEvent(expectedEvents[0])
            )

            val selectedTimeEntries = selector.select(state)(date)

            assertThat(selectedTimeEntries).hasSize(expectedTimeEntries.size + expectedEvents.size)
            // assertThat(selectedTimeEntries).allMatch {
            //     when (it) {
            //         is CalendarItem.TimeEntry -> it.timeEntry.description == "Expected"
            //         is CalendarItem.CalendarEvent -> it.calendarEvent.description == "Expected"
            //         is CalendarItem.SelectedItem -> it.description == "Expected"
            //     }
            // }
        }
    }
}