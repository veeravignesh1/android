package com.toggl.database.properties

data class NullableStringSyncProperty(
    val current: String?,
    val backup: String?,
    val status: PropertySyncStatus
)
