package com.toggl.timer.log.domain

import com.toggl.environment.services.TimeService
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import org.threeten.bp.Duration
import org.threeten.bp.format.DateTimeFormatter

private const val timeEntriesLogHeaderTimeFormat = "eee, dd MMM"

fun timeEntriesLogSelector(
    timeEntries: Map<Long, TimeEntry>,
    projects: Map<Long, Project>,
    timeService: TimeService,
    todayString: String,
    yesterdayString: String
): List<TimeEntryViewModel> {

    val today = timeService.now().toLocalDate()
    val yesterday = today.minusDays(1)

    return timeEntries.values
        .filter { it.duration != null }
        .sortedByDescending { it.startTime }
        .groupBy { timeEntry -> timeEntry.startTime.toLocalDate() }
        .flatMap { (groupDate, timeEntries) ->
            sequence {
                yield(
                    DayHeaderViewModel(
                        dayTitle = when (groupDate) {
                            today -> todayString
                            yesterday -> yesterdayString
                            else -> groupDate.format(DateTimeFormatter.ofPattern(timeEntriesLogHeaderTimeFormat))
                        },
                        totalDuration = timeEntries
                            .fold(Duration.ZERO) { acc, timeEntry -> acc + timeEntry.duration }
                    )
                )

                for (timeEntry in timeEntries) {
                    val projectId = timeEntry.projectId
                    val project =
                        if (projectId == null) null
                        else projects[projectId]?.run { ProjectViewModel(id, name, color) }

                    yield(
                        FlatTimeEntryViewModel(
                            id = timeEntry.id,
                            description = timeEntry.description,
                            startTime = timeEntry.startTime,
                            duration = timeEntry.duration
                                ?: throw IllegalStateException("You can't display a running time entry in the log"),
                            project = project,
                            billable = timeEntry.billable
                        )
                    )
                }
            }.toList()
        }
}