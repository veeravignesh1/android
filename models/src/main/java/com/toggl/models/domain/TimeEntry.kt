package com.toggl.models.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

@Entity(
    foreignKeys = [
        ForeignKey(entity = Project::class, parentColumns = ["id"], childColumns = ["projectId"])
    ],
    indices = [Index("projectId")]
)
data class TimeEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val startTime: OffsetDateTime,
    val duration: Duration?,
    val billable: Boolean,
    val projectId: Long?,
    val taskId: Long?,
    val isDeleted: Boolean
)
