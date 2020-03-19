package com.toggl.models.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Workspace::class, parentColumns = ["id"], childColumns = ["workspaceId"])
    ]
)
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val color: String,
    val active: Boolean,
    val isPrivate: Boolean,
    val billable: Boolean?,
    val workspaceId: Long,
    val clientId: Long?
)
