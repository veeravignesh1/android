package com.toggl.repository.timeentry

import com.toggl.database.dao.TimeEntryDao
import com.toggl.environment.services.TimeService
import com.toggl.models.domain.TimeEntry
import javax.inject.Inject
import org.threeten.bp.Duration

class TimeEntryRepositoryImpl @Inject constructor(
    private val timeEntryDao: TimeEntryDao,
    private val timeService: TimeService
) : TimeEntryRepository {

    override suspend fun loadTimeEntries() =
        timeEntryDao.getAll()

    override suspend fun startTimeEntry(description: String): StartTimeEntryResult {
        val stoppedTimeEntry = stopRunningTimeEntry()
        val id = timeEntryDao.insert(
            TimeEntry(
                description = description,
                startTime = timeService.now(),
                duration = null,
                billable = false,
                projectId = null,
                taskId = null,
                isDeleted = false
            )
        )
        return StartTimeEntryResult(
            timeEntryDao.getOne(id),
            stoppedTimeEntry
        )
    }

    override suspend fun stopRunningTimeEntry(): TimeEntry? {
        val now = timeService.now()
        return timeEntryDao
            .getAllRunning()
            .map { it.copy(duration = Duration.between(it.startTime, now)) }
            .also(timeEntryDao::updateAll)
            .firstOrNull()
    }

    override suspend fun editTimeEntry(timeEntry: TimeEntry): TimeEntry =
        timeEntryDao.update(timeEntry).run { timeEntry }

    override suspend fun deleteTimeEntries(timeEntries: List<TimeEntry>): HashSet<TimeEntry> =
        timeEntries
            .map { it.copy(isDeleted = true) }
            .apply(timeEntryDao::updateAll)
            .toHashSet()
}
