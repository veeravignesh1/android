package com.toggl.timer.startedit.ui.chips

import com.toggl.models.domain.Project as DomainProject
import com.toggl.models.domain.Tag as DomainTag

sealed class ChipViewModel(val text: String) {
    class AddTag(label: String) : ChipViewModel(label)
    class AddProject(label: String) : ChipViewModel(label)
    class Tag(val tag: DomainTag) : ChipViewModel(tag.name)
    class Project(val project: DomainProject) : ChipViewModel(project.name)
}
