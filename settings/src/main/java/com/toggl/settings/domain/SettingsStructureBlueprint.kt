package com.toggl.settings.domain

import androidx.annotation.StringRes
import com.toggl.models.domain.SettingsType
import com.toggl.settings.R

object SettingsStructureBlueprint {
    val sections = listOf(
        SettingsSectionBlueprint(
            R.string.your_profile, listOf(
            SettingsType.Name,
            SettingsType.Email,
            SettingsType.Workspace
        )),
        SettingsSectionBlueprint(R.string.date_and_time, listOf(
            SettingsType.DateFormat,
            SettingsType.TwentyFourHourClock,
            SettingsType.DurationFormat,
            SettingsType.FirstDayOfTheWeek,
            SettingsType.GroupSimilar
        )),
        SettingsSectionBlueprint(R.string.timer_defaults, listOf(
            SettingsType.CellSwipe,
            SettingsType.ManualMode
        )),
        SettingsSectionBlueprint(R.string.calendar_label, listOf(
            SettingsType.CalendarSettings,
            SettingsType.SmartAlert
        )),
        SettingsSectionBlueprint(R.string.general, listOf(
            SettingsType.SubmitFeedback,
            SettingsType.About,
            SettingsType.Help
        )),
        SettingsSectionBlueprint(R.string.sync, listOf(
            SettingsType.SignOut
        ))
    )
}

data class SettingsSectionBlueprint(@StringRes val title: Int, val settingsList: List<SettingsType>)