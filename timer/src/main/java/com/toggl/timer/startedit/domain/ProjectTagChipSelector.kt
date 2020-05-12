package com.toggl.timer.startedit.domain

import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.exceptions.ProjectDoesNotExistException
import com.toggl.timer.exceptions.TagDoesNotExistException
import com.toggl.timer.startedit.ui.chips.ChipViewModel

fun projectTagChipSelector(
    addProjectLabel: String,
    addTagLabel: String,
    editableTimeEntry: EditableTimeEntry,
    projects: Map<Long, Project>,
    tags: Map<Long, Tag>
): List<ChipViewModel> = sequence {

    if (editableTimeEntry.projectId != null) {
        val project = projects[editableTimeEntry.projectId] ?: throw ProjectDoesNotExistException()
        yield(ChipViewModel.Project(project))
    } else {
        yield(ChipViewModel.AddProject(addProjectLabel))
    }

    for (tagId in editableTimeEntry.tagIds) {
        val tag = tags[tagId] ?: throw TagDoesNotExistException()
        yield(ChipViewModel.Tag(tag))
    }

    yield(ChipViewModel.AddTag(addTagLabel))
}.toList()