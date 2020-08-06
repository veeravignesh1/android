package com.toggl.settings.ui.calendar

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.compose.getValue
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.layout.padding
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.toggl.models.domain.SettingsType
import com.toggl.settings.R
import com.toggl.common.feature.compose.ThemedPreview
import com.toggl.common.feature.compose.theme.TogglTheme
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsSectionViewModel
import com.toggl.settings.domain.SettingsViewModel
import com.toggl.settings.ui.common.SectionList
import com.toggl.settings.ui.common.SectionTitleMode
import kotlinx.coroutines.flow.Flow

@Composable
fun CalendarSettingsPage(
    calendarSettingsViewModels: Flow<List<SettingsSectionViewModel>>,
    statusBarHeight: Dp,
    navigationBarHeight: Dp,
    dispatcher: (SettingsAction) -> Unit
) {
    val observableState by calendarSettingsViewModels.collectAsState(listOf())
    TogglTheme {
        CalendarSettingsPageContent(
            observableState,
            statusBarHeight,
            navigationBarHeight,
            dispatcher
        )
    }
}

@Composable
fun CalendarSettingsPageContent(
    settingsViewModels: List<SettingsSectionViewModel>,
    statusBarHeight: Dp,
    navigationBarHeight: Dp,
    dispatcher: (SettingsAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = statusBarHeight),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { dispatcher.invoke(SettingsAction.FinishedEditingSetting) }) {
                        Icon(Icons.Filled.ArrowBack)
                    }
                }
            )
        },
        bodyContent = {
            SectionList(
                sectionsList = settingsViewModels,
                titleMode = SectionTitleMode.AllButFirst,
                dispatcher = dispatcher,
                navigationBarHeight = navigationBarHeight
            )
        }
    )
}

@Composable
@Preview("Settings page light theme")
fun PreviewCalendarSettingsPageLight() {
    ThemedPreview(false) {
        CalendarSettingsPageContent(calendarSettingsPreviewData, 10.dp, 10.dp) { }
    }
}

@Composable
@Preview("Settings page dark theme")
fun PreviewCalendarSettingsPageDark() {
    ThemedPreview(true) {
        CalendarSettingsPageContent(calendarSettingsPreviewData, 10.dp, 10.dp) { }
    }
}

val calendarSettingsPreviewData: List<SettingsSectionViewModel> = listOf(
    SettingsSectionViewModel(
        "",
        listOf(
            SettingsViewModel.Toggle("Link calendars", SettingsType.AllowCalendarAccess, true),
            SettingsViewModel.InfoText("Toggl needs access to your calendar in order to display events. Events are visible to you only and won’t appear in Reports.", SettingsType.CalendarPermissionInfo)
        )
    ),
    SettingsSectionViewModel(
        "someone@toggl.com",
        listOf(
            SettingsViewModel.Toggle("Meetings", SettingsType.Calendar("123", "123", true), true),
            SettingsViewModel.Toggle("Peer Reviews", SettingsType.Calendar("123", "123", true), false),
            SettingsViewModel.Toggle("Peer Reviews", SettingsType.Calendar("123", "123", true), false)
        )
    ),
    SettingsSectionViewModel(
        "team@toggl.com",
        listOf(
            SettingsViewModel.Toggle("Meetings", SettingsType.Calendar("123", "123", true), false),
            SettingsViewModel.Toggle("Peer Reviews", SettingsType.Calendar("123", "123", true), true),
            SettingsViewModel.Toggle("Peer Reviews", SettingsType.Calendar("123", "123", true), false)
        )
    )
)
