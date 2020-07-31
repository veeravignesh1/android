package com.toggl.settings.ui.about

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.padding
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.toggl.models.domain.SettingsType
import com.toggl.settings.R
import com.toggl.settings.compose.ThemedPreview
import com.toggl.settings.compose.theme.TogglTheme
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsSectionViewModel
import com.toggl.settings.domain.SettingsViewModel
import com.toggl.settings.ui.common.SectionList
import com.toggl.settings.ui.common.SectionTitleMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
@Composable
fun AboutPage(
    sectionsState: Flow<List<SettingsSectionViewModel>>,
    statusBarHeight: Dp,
    navigationBarHeight: Dp,
    dispatcher: (SettingsAction) -> Unit = {}
) {
    val observableSectionState by sectionsState.collectAsState(listOf())
    TogglTheme {
        AboutPageContent(observableSectionState, statusBarHeight, navigationBarHeight, dispatcher)
    }
}

@ExperimentalCoroutinesApi
@Composable
fun AboutPageContent(
    sectionsState: List<SettingsSectionViewModel>,
    statusBarHeight: Dp,
    navigationBarHeight: Dp,
    dispatcher: (SettingsAction) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = statusBarHeight),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                title = { Text(text = stringResource(R.string.about)) },
                navigationIcon = {
                    IconButton(onClick = { dispatcher(SettingsAction.FinishedEditingSetting) }) {
                        androidx.ui.foundation.Icon(androidx.ui.material.icons.Icons.Filled.ArrowBack)
                    }
                }
            )
        },
        bodyContent = {
            SectionList(
                sectionsList = sectionsState,
                titleMode = SectionTitleMode.None,
                dispatcher = dispatcher,
                navigationBarHeight = navigationBarHeight
            )
        }
    )
}

@ExperimentalCoroutinesApi
@Composable
@Preview("Settings page light theme")
fun PreviewCalendarSettingsPageLight() {
    ThemedPreview(false) {
        AboutPageContent(aboutPreviewData, 10.dp, 10.dp) { }
    }
}

@ExperimentalCoroutinesApi
@Composable
@Preview("Settings page dark theme")
fun PreviewCalendarSettingsPageDark() {
    ThemedPreview(true) {
        AboutPageContent(aboutPreviewData, 10.dp, 10.dp) { }
    }
}

val aboutPreviewData: List<SettingsSectionViewModel> = listOf(
    SettingsSectionViewModel(
        "Shouldn't be displayed",
        listOf(
            SettingsViewModel.SubPage("Terms of Service", SettingsType.TermsOfService),
            SettingsViewModel.SubPage("Privacy Policy", SettingsType.PrivacyPolicy),
            SettingsViewModel.SubPage("Licences", SettingsType.Licenses)
        )
    )
)
