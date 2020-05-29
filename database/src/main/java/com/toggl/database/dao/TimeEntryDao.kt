package com.toggl.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.toggl.database.models.DatabaseTimeEntry
import com.toggl.database.models.DatabaseTimeEntryTag
import com.toggl.database.models.DatabaseTimeEntryWithTags
import java.time.Duration
import java.time.OffsetDateTime

typealias StartTimeEntryDatabaseResult = Pair<DatabaseTimeEntry, List<DatabaseTimeEntry>>

@Dao
interface TimeEntryDao {
    // TimeEntries only

    @Transaction
    fun startTimeEntry(description: String, startTime: OffsetDateTime, billable: Boolean, workspaceId: Long, projectId: Long?, taskId: Long?): StartTimeEntryDatabaseResult {
        val stoppedTimeEntries = stopRunningTimeEntries(startTime)
        val id = insertTimeEntry(
            DatabaseTimeEntry(
                description = description,
                startTime = startTime,
                duration = null,
                billable = billable,
                workspaceId = workspaceId,
                projectId = projectId,
                taskId = taskId,
                isDeleted = false
            )
        )
        return getOneTimeEntry(id) to stoppedTimeEntries
    }

    @Transaction
    fun stopRunningTimeEntries(now: OffsetDateTime): List<DatabaseTimeEntry> {
        return getAllRunningTimeEntries()
            .map { it.copy(duration = Duration.between(it.startTime, now)) }
            .also(this::updateAllTimeEntries)
    }

    @Query("SELECT * FROM time_entries WHERE NOT isDeleted AND duration is null")
    fun getAllRunningTimeEntries(): List<DatabaseTimeEntry>

    @Query("SELECT * FROM time_entries WHERE NOT isDeleted AND id = :id")
    fun getOneTimeEntry(id: Long): DatabaseTimeEntry

    @Insert
    fun insertAllTimeEntries(vararg databaseTimeEntries: DatabaseTimeEntry): List<Long>

    @Insert
    fun insertTimeEntry(databaseTimeEntries: DatabaseTimeEntry): Long

    @Update
    fun updateTimeEntry(databaseTimeEntry: DatabaseTimeEntry)

    @Update
    fun updateAllTimeEntries(databaseTimeEntries: List<DatabaseTimeEntry>)

    @Delete
    fun deleteTimeEntry(databaseTimeEntry: DatabaseTimeEntry)

    // TimeEntries & Tags

    @Transaction
    @Query("SELECT * FROM time_entries WHERE NOT isDeleted")
    fun getAllTimeEntriesWithTags(): List<DatabaseTimeEntryWithTags>

    @Transaction
    @Update
    fun updateTimeEntryWithTags(databaseTimeEntryWithTags: DatabaseTimeEntryWithTags) {
        insertAllTimeEntryTagsPairs(databaseTimeEntryWithTags.tags.map {
            DatabaseTimeEntryTag(databaseTimeEntryWithTags.timeEntry.id, it)
        })

        updateTimeEntry(databaseTimeEntryWithTags.timeEntry)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllTimeEntryTagsPairs(databaseTimeEntryTags: List<DatabaseTimeEntryTag>)
}
