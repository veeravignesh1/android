package com.toggl.timer.startedit.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.models.domain.Tag
import com.toggl.repository.interfaces.TagRepository
import kotlinx.coroutines.withContext

class CreateTagEffect(
    private val dispatcherProvider: DispatcherProvider,
    private val repository: TagRepository,
    private val name: String,
    private val workspaceId: Long
) : Effect<StartEditAction.TagCreated> {
    override suspend fun execute(): StartEditAction.TagCreated? = withContext(dispatcherProvider.io) {
        val createdTag = repository.createTag(Tag(name = name, workspaceId = workspaceId))
        StartEditAction.TagCreated(createdTag)
    }
}