package com.toggl.timer.project.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.timer.project.domain.ProjectAction
import com.toggl.timer.project.domain.ProjectState

class ProjectStoreViewModel @ViewModelInject constructor(
    store: Store<ProjectState, ProjectAction>
) : ViewModel(), Store<ProjectState, ProjectAction> by store
