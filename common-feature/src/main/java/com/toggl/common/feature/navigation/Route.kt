package com.toggl.common.feature.navigation

import android.content.Context
import android.net.Uri
import com.toggl.common.deepLinks
import com.toggl.common.feature.models.SelectedCalendarItem
import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.EditableTimeEntry

sealed class Route {
    object Onboarding : Route()
    object Timer : Route()
    class StartEdit(val editableTimeEntry: EditableTimeEntry) : Route()
    class Project(val editableProject: EditableProject) : Route()
    class ContextualMenu(val selectedItem: SelectedCalendarItem) : Route()
    object Settings : Route()
}

fun Route.deepLink(context: Context): Uri {

    val deepLinks = context.deepLinks

    return when (this) {
        Route.Onboarding -> TODO()
        Route.Timer -> deepLinks.timeEntriesLog
        is Route.StartEdit -> deepLinks.timeEntriesStartEditDialog
        is Route.Project -> deepLinks.timeEntriesProjectDialog
        is Route.ContextualMenu -> TODO()
        Route.Settings -> TODO()
    }
}