package com.toggl.common

import android.content.res.Resources
import android.net.Uri
import androidx.core.net.toUri
import com.toggl.common.android.R

class DeepLinkUrls(
    val welcome: Uri,
    val login: Uri,
    val passwordReset: Uri,
    val timeEntriesLog: Uri,
    val startEditDialog: Uri,
    val projectDialog: Uri,
    val reports: Uri,
    val calendar: Uri,
    val contextualMenu: Uri,
    val settings: Uri,
    val calendarSettings: Uri,
    val about: Uri,
    val singleChoiceDialog: Uri,
    val submitFeedback: Uri
) {

    companion object {
        fun fromResources(resources: Resources) =
            with(resources) {
                DeepLinkUrls(
                    timeEntriesLog = getString(R.string.deep_link_time_entries_log).toUri(),
                    startEditDialog = getString(R.string.deep_link_start_edit_dialog).toUri(),
                    projectDialog = getString(R.string.deep_link_project_dialog).toUri(),
                    calendar = getString(R.string.deep_link_calendar).toUri(),
                    reports = getString(R.string.deep_link_reports).toUri(),
                    welcome = getString(R.string.deep_link_welcome).toUri(),
                    login = getString(R.string.deep_link_login).toUri(),
                    passwordReset = getString(R.string.deep_link_password_reset).toUri(),
                    settings = getString(R.string.deep_link_settings).toUri(),
                    contextualMenu = getString(R.string.deep_link_contextual_menu).toUri(),
                    calendarSettings = getString(R.string.deep_link_settings_calendar_settings).toUri(),
                    submitFeedback = getString(R.string.deep_link_settings_submit_feedback).toUri(),
                    about = getString(R.string.deep_link_settings_about).toUri(),
                    singleChoiceDialog = getString(R.string.deep_link_settings_single_choice).toUri()
                )
            }
    }
}