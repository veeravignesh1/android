package com.toggl.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    foreignKeys = [ ForeignKey(entity = DatabaseWorkspace::class, parentColumns = ["id"], childColumns = ["defaultWorkspaceId"]) ],
    indices = [Index("defaultWorkspaceId")]
)
data class DatabaseUser(
    @PrimaryKey
    val id: Long = 0,
    val apiToken: String,
    val email: String,
    val name: String,
    val defaultWorkspaceId: Long
)
