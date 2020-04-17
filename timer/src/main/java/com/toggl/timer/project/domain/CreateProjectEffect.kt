package com.toggl.timer.project.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.dto.CreateProjectDTO
import com.toggl.repository.interfaces.ProjectRepository
import kotlinx.coroutines.withContext

class CreateProjectEffect(
    private val projectDTO: CreateProjectDTO,
    private val projectRepository: ProjectRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<ProjectAction> {
    override suspend fun execute(): ProjectAction? = withContext(dispatcherProvider.io) {
        val createdProject = projectRepository.createProject(projectDTO)
        ProjectAction.ProjectCreated(createdProject)
    }
}