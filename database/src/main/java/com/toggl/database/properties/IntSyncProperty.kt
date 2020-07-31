package com.toggl.database.properties

data class IntSyncProperty(
    val current: Int,
    val backup: Int,
    val status: PropertySyncStatus
)
