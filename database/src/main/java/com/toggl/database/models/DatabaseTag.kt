package com.toggl.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    foreignKeys = [
        ForeignKey(entity = DatabaseWorkspace::class, parentColumns = ["id"], childColumns = ["workspaceId"])
    ],
    indices = [ Index("workspaceId") ]
)
data class DatabaseTag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serverId: Long?,
    val name: String,
    val workspaceId: Long
)
