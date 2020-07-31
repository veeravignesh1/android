package com.toggl.database.properties

data class BooleanSyncProperty(
    val current: Boolean,
    val backup: Boolean,
    val status: PropertySyncStatus
)
