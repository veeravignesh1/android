package com.toggl.repository.interfaces

import com.toggl.models.domain.Project

interface ProjectRepository {
    suspend fun loadProjects(): List<Project>
}