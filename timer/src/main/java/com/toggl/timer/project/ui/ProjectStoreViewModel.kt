package com.toggl.timer.project.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import com.toggl.timer.project.domain.ProjectAction
import com.toggl.timer.project.domain.ProjectState
import javax.inject.Inject

class ProjectStoreViewModel @Inject constructor(
    store: Store<ProjectState, ProjectAction>
) : ViewModel(), Store<ProjectState, ProjectAction> by store