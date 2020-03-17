package com.toggl.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val color: String,
    val active: Boolean,
    val isPrivate: Boolean,
    val billable: Boolean?,
    val clientId: Long?
)
