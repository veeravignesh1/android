package com.toggl.settings.ui.composables

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.ripple.RippleIndication
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.toggl.models.domain.SettingsType
import com.toggl.settings.R
import com.toggl.settings.compose.ThemedPreview
import com.toggl.settings.domain.SettingsAction

data class AboutPageItem(val title: Int, val action: SettingsAction)

private val items = listOf(
    AboutPageItem(
        title = R.string.privacy_policy,
        action = SettingsAction.SettingTapped(selectedSetting = SettingsType.PrivacyPolicy)),
    AboutPageItem(
        title = R.string.terms_of_service,
        action = SettingsAction.SettingTapped(selectedSetting = SettingsType.TermsOfService)),
    AboutPageItem(
        title = R.string.licenses,
        action = SettingsAction.SettingTapped(selectedSetting = SettingsType.Licenses))
)

@Composable
internal fun AboutPage(
    dispatch: (SettingsAction) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                title = { Text(stringResource(R.string.about)) }
            )
        },
        bodyContent = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                for (item in items) {
                    SettingsRow(
                        modifier = Modifier.clickable(
                            indication = RippleIndication(),
                            onClick = { dispatch(item.action) })
                    ) {
                        Text(stringResource(item.title))
                    }
                    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
                }
            }
        }
    )
}

@Preview("About light theme")
@Composable
fun PreviewAboutPageLight() {
    ThemedPreview {
        AboutPage()
    }
}

@Preview("About dark theme")
@Composable
fun PreviewAboutPageDark() {
    ThemedPreview(darkTheme = true) {
        AboutPage()
    }
}