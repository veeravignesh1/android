package com.toggl.repository.interfaces

import com.toggl.models.domain.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    suspend fun createTag(tag: Tag): Tag
    fun loadTags(): Flow<List<Tag>>
}
