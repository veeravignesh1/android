package com.toggl.repository.interfaces

import com.toggl.models.domain.Task

interface TaskRepository {
    suspend fun loadTasks(): List<Task>
}