package com.toggl.repository.interfaces

interface AppRepository {
    suspend fun clearAllData()
}
