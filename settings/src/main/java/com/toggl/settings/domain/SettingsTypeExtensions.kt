package com.toggl.settings.domain

import android.content.Context
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SettingsType
import com.toggl.models.domain.UserPreferences
import com.toggl.settings.R
import java.time.DayOfWeek

fun SettingsType.toPresentableList(context: Context, userPreferences: UserPreferences): Pair<String, List<ChoiceListItem>> {
    val settingHeader: String
    val settingChoiceListItems: List<ChoiceListItem>

    when (this) {
        SettingsType.DateFormat -> {
            settingHeader = context.getString(R.string.date_format)
            settingChoiceListItems =
                DateFormat.values().map {
                    ChoiceListItem(
                        it.label,
                        userPreferences.dateFormat == it,
                        selectedAction = SettingsAction.DateFormatSelected(it)
                    )
                }
        }
        SettingsType.DurationFormat -> {
            settingHeader = context.getString(R.string.duration_format)
            settingChoiceListItems =
                DurationFormat.values().map {
                    ChoiceListItem(
                        it.getTranslatedRepresentation(context),
                        userPreferences.durationFormat == it,
                        selectedAction = SettingsAction.DurationFormatSelected(it)
                    )
                }
        }
        SettingsType.FirstDayOfTheWeek -> {
            settingHeader = context.getString(R.string.first_day_of_the_week)
            settingChoiceListItems =
                DayOfWeek.values().map {
                    ChoiceListItem(
                        it.getTranslatedRepresentation(context),
                        userPreferences.firstDayOfTheWeek == it,
                        selectedAction = SettingsAction.FirstDayOfTheWeekSelected(it)
                    )
                }
        }
        SettingsType.Workspace -> {
            settingHeader = context.getString(R.string.default_workspace)
            settingChoiceListItems = listOf()
        }
        else -> {
            settingHeader = "UNKNOWN"
            settingChoiceListItems = listOf()
        }
    }

    return Pair(settingHeader, settingChoiceListItems)
}