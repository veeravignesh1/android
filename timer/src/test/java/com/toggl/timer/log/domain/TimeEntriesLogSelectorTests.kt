package com.toggl.timer.log.domain

import com.toggl.environment.services.TimeService
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.common.createTimeEntry
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.mockk.every
import io.mockk.mockk
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class TimeEntriesLogSelectorTests : FreeSpec({

    val todayString = "Today"
    val yesterdayString = "Yesterday"
    val timeService = mockk<TimeService>()
    val today = OffsetDateTime.now()
    val localToday = today.toLocalDate()
    val yesterday = localToday.minusDays(1)

    val selectorToTest: (Map<Long, TimeEntry>, Map<Long, Project>) -> List<TimeEntryViewModel> =
        { timeEntries, projects ->
            timeEntriesLogSelector(timeEntries, projects, timeService, todayString, yesterdayString)
        }

    every { timeService.now() } returns today

    val timeEntries = (0L..10L).flatMap { daysInThePast ->
        val date = today.minusDays(daysInThePast)
        (0L..10L).map { id ->
            createTimeEntry(
                id = daysInThePast + id,
                description = "Time entry $id",
                startTime = date,
                duration = Duration.ofMinutes(30)
            )
        }
    }

    val timeEntriesMap = timeEntries.associateBy { it.id }
    val projectsMap = mapOf<Long, Project>()

    "The TimeEntriesLogSelector" - {
        "does not" - {
            "add running time entries to the groups" - {
                val timeEntriesButAllRunning = timeEntries.map { it.copy(duration = null) }
                val groupedTimeEntries = selectorToTest(
                    timeEntriesButAllRunning.associateBy { it.id },
                    projectsMap
                )

                groupedTimeEntries.size shouldBe 0
            }

            "create headers for days that have no time entries" - {

                val groupedTimeEntries = selectorToTest(
                    timeEntriesMap,
                    projectsMap
                )

                val headers =
                    groupedTimeEntries.filterIsInstance<DayHeaderViewModel>().map { it.dayTitle }

                val flatTimeEntryHeaders =
                    groupedTimeEntries
                        .asSequence()
                        .filterIsInstance<FlatTimeEntryViewModel>()
                        .map { it.startTime.toLocalDate() }
                        .distinct()
                        .map {
                            when (it) {
                                localToday -> todayString
                                yesterday -> yesterdayString
                                else -> it.format(
                                    DateTimeFormatter.ofPattern("eee, dd MMM")
                                )
                            }
                        }.distinct().toList()

                headers shouldContainExactlyInAnyOrder flatTimeEntryHeaders
            }
        }

        "the header titles are formatted" - {

            "as the localized string for Today when the header is for the current day" - {
                val groupedTimeEntries = selectorToTest(
                    timeEntriesMap,
                    projectsMap
                )

                val headerTitles =
                    groupedTimeEntries.filterIsInstance<DayHeaderViewModel>().map { it.dayTitle }

                headerTitles shouldContain todayString
            }

            "as the localized string for Today when the header is for the previous day" - {
                val groupedTimeEntries = selectorToTest(
                    timeEntriesMap,
                    projectsMap
                )

                val headerTitles =
                    groupedTimeEntries.filterIsInstance<DayHeaderViewModel>().map { it.dayTitle }

                headerTitles shouldContain yesterdayString
            }

            "using eee, dd MMM for all other days" - {
                val formatter = DateTimeFormatter.ofPattern("eee, dd MMM")

                val groupedTimeEntries = selectorToTest(
                    timeEntriesMap,
                    projectsMap
                )

                val headerTitles =
                    groupedTimeEntries.filterIsInstance<DayHeaderViewModel>()
                        .map { it.dayTitle }
                        .filter { it != todayString && it != yesterdayString }
                        .distinct()

                val formattedDates =
                    groupedTimeEntries
                        .asSequence()
                        .filterIsInstance<FlatTimeEntryViewModel>()
                        .map { it.startTime.toLocalDate() }
                        .distinct()
                        .sortedDescending()
                        .drop(2)
                        .map { it.format(formatter) }
                        .toList()

                headerTitles shouldContainExactlyInAnyOrder formattedDates
            }
        }
    }
})