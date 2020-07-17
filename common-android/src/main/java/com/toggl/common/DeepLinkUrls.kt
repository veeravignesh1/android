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
    val welcome: Uri,
    val login: Uri,
    val contextualMenu: Uri,
    val settings: Uri,
    val workspace: Uri,
    val dateFormat: Uri,
    val durationFormat: Uri,
    val firstDayOfTheWeek: Uri,
    val calendarSettings: Uri,
    val smartAlert: Uri,
    val submitFeedback: Uri,
    val about: Uri,
    val privacyPolicy: Uri,
    val termsOfService: Uri,
    val licenses: Uri,
    val help: Uri
) {

    companion object {
        fun fromResources(resources: Resources) =
            with(resources) {
                DeepLinkUrls(
                    main = getString(R.string.deep_link_main).toUri(),
                    timeEntriesLog = getString(R.string.deep_link_time_entries_log).toUri(),
                    startEditDialog = getString(R.string.deep_link_start_edit_dialog).toUri(),
                    projectDialog = getString(R.string.deep_link_project_dialog).toUri(),
                    calendar = getString(R.string.deep_link_calendar).toUri(),
                    reports = getString(R.string.deep_link_reports).toUri(),
                    welcome = getString(R.string.deep_link_welcome).toUri(),
                    login = getString(R.string.deep_link_login).toUri(),
                    settings = getString(R.string.deep_link_settings).toUri(),
                    workspace = getString(R.string.deep_link_settings_workspace).toUri(),
                    contextualMenu = getString(R.string.deep_link_contextual_menu).toUri(),
                    dateFormat = getString(R.string.deep_link_settings_date_format).toUri(),
                    durationFormat = getString(R.string.deep_link_settings_duration_format).toUri(),
                    firstDayOfTheWeek = getString(R.string.deep_link_settings_first_day_of_the_week).toUri(),
                    calendarSettings = getString(R.string.deep_link_settings_calendar_settings).toUri(),
                    smartAlert = getString(R.string.deep_link_settings_smart_alert).toUri(),
                    submitFeedback = getString(R.string.deep_link_settings_submit_feedback).toUri(),
                    about = getString(R.string.deep_link_settings_about).toUri(),
                    privacyPolicy = getString(R.string.deep_link_settings_privacy_policy).toUri(),
                    termsOfService = getString(R.string.deep_link_settings_terms_of_service).toUri(),
                    licenses = getString(R.string.deep_link_settings_licenses).toUri(),
                    help = getString(R.string.deep_link_settings_help).toUri()
                )
            }
    }
}