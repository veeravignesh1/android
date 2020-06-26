package com.toggl.common

import android.content.res.Resources
import android.net.Uri
import androidx.core.net.toUri
import com.toggl.common.android.R

class DeepLinkUrls(
    val main: Uri,
    val timeEntriesLog: Uri,
    val startEditDialog: Uri,
    val projectDialog: Uri,
    val calendar: Uri,
    val reports: Uri,
    val onboarding: Uri,
    val settings: Uri,
    val contextualMenu: Uri
) {

    companion object {
        fun fromResources(resources: Resources) =
            DeepLinkUrls(
                main = resources.getString(R.string.deep_link_main).toUri(),
                timeEntriesLog = resources.getString(R.string.deep_link_time_entries_log).toUri(),
                startEditDialog = resources.getString(R.string.deep_link_start_edit_dialog).toUri(),
                projectDialog = resources.getString(R.string.deep_link_project_dialog).toUri(),
                calendar = resources.getString(R.string.deep_link_calendar).toUri(),
                reports = resources.getString(R.string.deep_link_reports).toUri(),
                onboarding = resources.getString(R.string.deep_link_onboarding).toUri(),
                settings = resources.getString(R.string.deep_link_settings).toUri(),
                contextualMenu = resources.getString(R.string.deep_link_contextual_menu).toUri()
            )
    }
}