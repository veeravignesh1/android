package com.toggl.repository.interfaces

import com.toggl.models.domain.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun loadClients(): Flow<List<Client>>
    suspend fun createClient(client: Client): Client
}
