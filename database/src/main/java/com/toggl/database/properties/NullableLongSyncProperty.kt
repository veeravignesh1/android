package com.toggl.database.properties

data class NullableLongSyncProperty(
    val current: Long?,
    val backup: Long?,
    val status: PropertySyncStatus
)
