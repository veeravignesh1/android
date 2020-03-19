package com.toggl.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workspace(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val features: List<WorkspaceFeature>
)