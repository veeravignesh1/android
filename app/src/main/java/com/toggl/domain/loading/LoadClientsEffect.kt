package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.ClientRepository
import kotlinx.coroutines.withContext

class LoadClientsEffect(
    private val repository: ClientRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<LoadingAction> {
    override suspend fun execute(): LoadingAction? =
        withContext(dispatcherProvider.io) {
            LoadingAction.ClientsLoaded(repository.loadClients())
        }
}
