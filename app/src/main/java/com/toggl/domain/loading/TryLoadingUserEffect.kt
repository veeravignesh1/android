package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.repository.interfaces.UserRepository
import kotlinx.coroutines.withContext

class TryLoadingUserEffect(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : Effect<LoadingAction> {

    override suspend fun execute(): LoadingAction? = withContext(dispatcherProvider.io) {
        val user = userRepository.get()
        LoadingAction.UserLoaded(user)
    }
}
