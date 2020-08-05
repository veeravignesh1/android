package com.toggl.settings.domain

import com.toggl.common.feature.services.calendar.CalendarService
import com.toggl.common.services.permissions.PermissionCheckerService
import com.toggl.models.domain.SettingsType
import com.toggl.settings.R
import com.toggl.settings.compose.ResOrStr
import com.toggl.settings.compose.ResOrStr.Empty
import com.toggl.settings.compose.ResOrStr.Res
import com.toggl.settings.compose.ResOrStr.Str
import javax.inject.Inject

class SettingsStructureBlueprint @Inject constructor(
    private val calendarService: CalendarService,
    private val permissionCheckerService: PermissionCheckerService
) {

    suspend fun calendarSections(state: SettingsState): List<SettingsSectionBlueprint> {

        val userCalendars = calendarService.getUserSelectedCalendars(state.userPreferences)
        val availableCalendars = calendarService.getAvailableCalendars()

        val calendarIntegrationEnabled = permissionCheckerService.hasCalendarPermission() &&
            state.userPreferences.calendarIntegrationEnabled

        val headerSection = SettingsSectionBlueprint(
            Empty,
            listOf(
                SettingsType.AllowCalendarAccess,
                SettingsType.CalendarPermissionInfo
            )
        )

        if (!calendarIntegrationEnabled) return listOf(headerSection)

        val calendarSections = availableCalendars
            .groupBy { it.sourceName }
            .map { (groupName, calendars) ->
                SettingsSectionBlueprint(
                    Str(groupName),
                    calendars.map {
                        SettingsType.Calendar(it.name, it.id, userCalendars.contains(it))
                    }
                )
            }

        return listOf(headerSection) + calendarSections
    }

    companion object {
        val mainSections = listOf(
            SettingsSectionBlueprint(
                Res(R.string.your_profile),
                listOf(
                    SettingsType.TextSetting.Name,
                    SettingsType.TextSetting.Email,
                    SettingsType.Workspace
                )
            ),
            SettingsSectionBlueprint(
                Res(R.string.date_and_time),
                listOf(
                    SettingsType.DateFormat,
                    SettingsType.TwentyFourHourClock,
                    SettingsType.DurationFormat,
                    SettingsType.FirstDayOfTheWeek,
                    SettingsType.GroupSimilar
                )
            ),
            SettingsSectionBlueprint(
                Res(R.string.timer_defaults),
                listOf(
                    SettingsType.CellSwipe,
                    SettingsType.ManualMode
                )
            ),
            SettingsSectionBlueprint(
                Res(R.string.calendar_label),
                listOf(
                    SettingsType.CalendarSettings,
                    SettingsType.SmartAlert
                )
            ),
            SettingsSectionBlueprint(
                Res(R.string.general),
                listOf(
                    SettingsType.SubmitFeedback,
                    SettingsType.About,
                    SettingsType.Help
                )
            ),
            SettingsSectionBlueprint(
                Res(R.string.sync),
                listOf(
                    SettingsType.SignOut
                )
            )
        )

        val aboutSection =
            SettingsSectionBlueprint(
                Empty,
                listOf(
                    SettingsType.PrivacyPolicy,
                    SettingsType.TermsOfService,
                    SettingsType.Licenses
                )
            )
    }
}

data class SettingsSectionBlueprint(
    val title: ResOrStr,
    val settingsList: List<SettingsType>
)
