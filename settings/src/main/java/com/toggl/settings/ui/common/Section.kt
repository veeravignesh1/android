package com.toggl.settings.ui.common

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.height
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.RippleIndication
import androidx.ui.res.stringResource
import androidx.ui.unit.dp
import com.toggl.models.domain.SettingsType
import com.toggl.settings.R
import com.toggl.common.feature.compose.theme.grid_1
import com.toggl.common.feature.compose.theme.grid_2
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsSectionViewModel
import com.toggl.settings.domain.SettingsViewModel

@Composable
fun Section(
    section: SettingsSectionViewModel,
    withTitle: Boolean,
    modifier: Modifier = Modifier,
    dispatcher: (SettingsAction) -> Unit
) {
    val columnModifier = Modifier
        .padding(top = grid_1, bottom = grid_1)
        .plus(modifier)

    Column(modifier = columnModifier) {
        if (withTitle) {
            Text(
                text = section.title,
                modifier = Modifier.fillMaxWidth().padding(grid_2),
                style = MaterialTheme.typography.subtitle1
            )
        }
        for (settingsOption in section.settingsOptions) {
            SettingsRow(settingsOption, dispatcher)
        }
    }
}

@Composable
internal fun SettingsRow(
    setting: SettingsViewModel,
    dispatcher: (SettingsAction) -> Unit
) {

    val tapAction = when (val settingsType = setting.settingsType) {
        SettingsType.Name -> null
        SettingsType.Email -> null
        SettingsType.TwentyFourHourClock -> SettingsAction.Use24HourClockToggled
        SettingsType.GroupSimilar -> SettingsAction.GroupSimilarTimeEntriesToggled
        SettingsType.CellSwipe -> SettingsAction.CellSwipeActionsToggled
        SettingsType.ManualMode -> SettingsAction.ManualModeToggled
        SettingsType.CalendarSettings -> SettingsAction.OpenCalendarSettingsTapped
        SettingsType.SubmitFeedback -> SettingsAction.OpenSubmitFeedbackTapped
        SettingsType.About -> SettingsAction.OpenAboutTapped
        SettingsType.PrivacyPolicy -> SettingsAction.OpenPrivacyPolicyTapped
        SettingsType.TermsOfService -> SettingsAction.OpenTermsOfServiceTapped
        SettingsType.Licenses -> SettingsAction.OpenLicencesTapped
        SettingsType.Help -> SettingsAction.OpenHelpTapped
        SettingsType.SignOut -> SettingsAction.SignOutTapped
        SettingsType.AllowCalendarAccess -> SettingsAction.AllowCalendarAccessToggled
        is SettingsType.Calendar -> SettingsAction.UserCalendarIntegrationToggled(settingsType.id)
        SettingsType.CalendarPermissionInfo -> null
        is SettingsType.SingleChoiceSetting -> SettingsAction.OpenSelectionDialog(settingsType)
    }

    val onClick = tapAction?.let { { dispatcher(it) } }

    val clickModifier =
        if (onClick != null)
            Modifier.clickable(
                indication = RippleIndication(),
                onClick = onClick
            )
        else Modifier

    val rowModifier = Modifier
        .height(48.dp)
        .fillMaxWidth() + clickModifier

    Row(
        modifier = rowModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalGravity = Alignment.CenterVertically
    ) {
        when (setting) {
            is SettingsViewModel.Toggle -> ToggleSetting(setting, onClick ?: {})
            is SettingsViewModel.ListChoice -> ListChoiceSetting(setting)
            is SettingsViewModel.SubPage -> SubPageSetting(setting)
            is SettingsViewModel.ActionRow -> ActionSetting(setting)
            is SettingsViewModel.InfoText -> Text(
                text = stringResource(R.string.allow_calendar_message),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(grid_2)
            )
        }
    }
}
