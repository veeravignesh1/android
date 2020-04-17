package com.toggl.repository.interfaces

import com.toggl.models.domain.Project
import com.toggl.repository.dto.CreateProjectDTO

interface ProjectRepository {
    suspend fun createProject(project: CreateProjectDTO): Project
    suspend fun loadProjects(): List<Project>
}