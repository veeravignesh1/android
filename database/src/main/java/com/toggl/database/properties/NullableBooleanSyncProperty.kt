package com.toggl.database.properties

data class NullableBooleanSyncProperty(
    val current: Boolean?,
    val backup: Boolean?,
    val status: PropertySyncStatus
)
