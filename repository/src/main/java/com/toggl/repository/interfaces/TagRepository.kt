package com.toggl.repository.interfaces

import com.toggl.models.domain.Tag

interface TagRepository {
    suspend fun createTag(tag: Tag): Tag
    suspend fun loadTags(): List<Tag>
}