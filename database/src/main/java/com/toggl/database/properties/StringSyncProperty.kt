package com.toggl.database.properties

data class StringSyncProperty(
    val current: String,
    val backup: String,
    val status: PropertySyncStatus
)
