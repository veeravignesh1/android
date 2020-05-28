package com.toggl.repository.interfaces

import com.toggl.models.domain.TimeEntry
import com.toggl.repository.dto.CreateTimeEntryDTO
import com.toggl.repository.dto.StartTimeEntryDTO

data class StartTimeEntryResult(
    val startedTimeEntry: TimeEntry,
    val stoppedTimeEntry: TimeEntry?
)

interface TimeEntryRepository {
    suspend fun loadTimeEntries(): List<TimeEntry>
    suspend fun startTimeEntry(startTimeEntryDTO: StartTimeEntryDTO): StartTimeEntryResult
    suspend fun createTimeEntry(createTimeEntryDTO: CreateTimeEntryDTO): TimeEntry
    suspend fun stopRunningTimeEntry(): TimeEntry?
    suspend fun editTimeEntry(timeEntry: TimeEntry): TimeEntry
    suspend fun deleteTimeEntry(timeEntry: TimeEntry): TimeEntry
}
