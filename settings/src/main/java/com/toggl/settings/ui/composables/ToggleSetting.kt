package com.toggl.settings.ui.composables

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Switch
import androidx.ui.tooling.preview.Preview
import com.toggl.models.domain.SettingsType
import com.toggl.settings.compose.ThemedPreview
import com.toggl.settings.domain.SettingsViewModel

@Composable
internal fun ToggleSetting(
    model: SettingsViewModel.Toggle,
    onClickAction: () -> Unit
) {
    Text(
        text = model.label,
        style = MaterialTheme.typography.body2
    )
    Switch(
        checked = model.toggled,
        color = MaterialTheme.colors.primary,
        onCheckedChange = { onClickAction() }
    )
}

internal val previewToggle = SettingsViewModel.Toggle("Toggle title", SettingsType.ManualMode, false)

@Preview("Toggle with light theme")
@Composable
fun PreviewToggleLight() {
    ThemedPreview {
        Column {
            SettingsRow {
                ToggleSetting(previewToggle) {}
            }
            SettingsRow {
                ToggleSetting(previewToggle.copy(toggled = true)) {}
            }
        }
    }
}

@Preview("Toggle with dark theme")
@Composable
fun PreviewToggleDark() {
    ThemedPreview(darkTheme = true) {
        Column {
            SettingsRow {
                ToggleSetting(previewToggle) {}
            }
            SettingsRow {
                ToggleSetting(previewToggle.copy(toggled = true)) {}
            }
        }
    }
}