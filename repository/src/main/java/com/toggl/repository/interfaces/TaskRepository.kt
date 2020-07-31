package com.toggl.repository.interfaces

import com.toggl.models.domain.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun loadTasks(): Flow<List<Task>>
}
