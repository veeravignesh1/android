package com.toggl.common.feature.navigation

import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.EditableTimeEntry

typealias BackStack = List<Route>

fun BackStack.push(route: Route) =
    this + route

fun BackStack.pop() =
    this.dropLast(1)

fun BackStack.getEditableTimeEntryIfAny() =
    filterIsInstance<Route.StartEdit>().firstOrNull()?.editableTimeEntry

fun BackStack.updateEditableTimeEntry(editableTimeEntry: EditableTimeEntry): BackStack =
    map {
        if (it !is Route.StartEdit) it
        else Route.StartEdit(editableTimeEntry)
    }

fun BackStack.getEditableProjectIfAny() =
    filterIsInstance<Route.Project>().firstOrNull()?.editableProject

fun BackStack.updateEditableProject(editableProject: EditableProject): BackStack =
    map {
        if (it !is Route.Project) it
        else Route.Project(editableProject)
    }

fun BackStack.getSelectedItemIfAny() =
    filterIsInstance<Route.ContextualMenu>().firstOrNull()?.selectedItem

fun BackStack.updateSelectableItem(selectedItem: SelectedCalendarItem): BackStack =
    map {
        if (it !is Route.ContextualMenu) it
        else Route.ContextualMenu(selectedItem)
    }