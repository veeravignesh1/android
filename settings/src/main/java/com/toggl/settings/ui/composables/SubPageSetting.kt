package com.toggl.settings.ui.composables

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.toggl.models.domain.SettingsType
import com.toggl.settings.compose.ThemedPreview
import com.toggl.settings.domain.SettingsViewModel

@Composable
internal fun SubPageSetting(
    model: SettingsViewModel.SubPage
) {
    Text(
        text = model.label,
        style = MaterialTheme.typography.body2
    )
}

internal val previewSubPage = SettingsViewModel.SubPage("Help", SettingsType.Help)

@Preview("SubPage light theme")
@Composable
fun PreviewSubPageLight() {
    ThemedPreview {
        SettingsRow {
            SubPageSetting(previewSubPage)
        }
    }
}

@Preview("SubPage dark theme")
@Composable
fun PreviewSubPageDark() {
    ThemedPreview(darkTheme = true) {
        SettingsRow {
            SubPageSetting(previewSubPage)
        }
    }
}