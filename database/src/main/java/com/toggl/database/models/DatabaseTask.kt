package com.toggl.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(entity = DatabaseWorkspace::class, parentColumns = ["id"], childColumns = ["workspaceId"]),
        ForeignKey(entity = DatabaseProject::class, parentColumns = ["id"], childColumns = ["projectId"])
    ],
    indices = [ Index("workspaceId"), Index("projectId") ]
)
data class DatabaseTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val active: Boolean,
    val projectId: Long,
    val workspaceId: Long,
    val userId: Long?
)
