package com.toggl.timer.project.domain

import com.toggl.architecture.core.Selector
import com.toggl.models.domain.Project
import com.toggl.models.extensions.isPro
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ProjectColorSelector @Inject constructor() : Selector<ProjectState, List<ColorViewModel>> {
    override suspend fun select(state: ProjectState): List<ColorViewModel> = sequence {

        val selectedColor = state.editableProject.color
        val defaultColorViewModels = Project.defaultColors.map {
            ColorViewModel.DefaultColor(it, it.equals(selectedColor, true))
        }
        yieldAll(defaultColorViewModels)

        val workspaceIsPro = state.workspaces[state.editableProject.workspaceId]?.isPro() ?: false
        val customColorSelected = workspaceIsPro && defaultColorViewModels.none { it.selected }
        yield(if (workspaceIsPro) ColorViewModel.CustomColor(customColorSelected) else ColorViewModel.PremiumLocked)
    }.toList()
}
