package com.toggl.models.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Workspace::class, parentColumns = ["id"], childColumns = ["workspaceId"])
    ]
)
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val workspaceId: Long
)