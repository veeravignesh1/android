package com.toggl.timer.project.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.models.domain.Client
import com.toggl.repository.interfaces.ClientRepository
import kotlinx.coroutines.withContext

class CreateClientEffect(
    private val dispatcherProvider: DispatcherProvider,
    private val repository: ClientRepository,
    private val name: String,
    private val workspaceId: Long
) : Effect<ProjectAction.ClientCreated> {
    override suspend fun execute(): ProjectAction.ClientCreated? = withContext(dispatcherProvider.io) {
        val createdClient = repository.createClient(Client(name = name, workspaceId = workspaceId))
        ProjectAction.ClientCreated(createdClient)
    }
}