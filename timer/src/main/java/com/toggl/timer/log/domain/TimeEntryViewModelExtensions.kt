package com.toggl.timer.log.domain

import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import org.threeten.bp.Duration

fun TimeEntry.toFlatTimeEntryViewModel(projects: Map<Long, Project>) =
    FlatTimeEntryViewModel(
        id = id,
        description = description,
        startTime = startTime,
        duration = duration ?: throw IllegalStateException("Running time entries are not supported"),
        project = projects.getProjectViewModelFor(this),
        billable = billable
    )

fun List<TimeEntry>.toTimeEntryGroupViewModel(projects: Map<Long, Project>) =
    TimeEntryGroupViewModel(
        timeEntryIds = map(TimeEntry::id),
        isExpanded = false,
        description = first().description,
        duration = totalDuration(),
        project = projects.getProjectViewModelFor(this.first()),
        billable = first().billable
    )

fun Project.toProjectViewModel() = ProjectViewModel(id, name, color)

fun List<TimeEntry>.totalDuration(): Duration =
    fold(Duration.ZERO) { acc, timeEntry -> acc + timeEntry.duration }

private fun Map<Long, Project>.getProjectViewModelFor(timeEntry: TimeEntry): ProjectViewModel? {
    val projectId = timeEntry.projectId
    return if (projectId == null) null
    else this[projectId]?.run(Project::toProjectViewModel)
}