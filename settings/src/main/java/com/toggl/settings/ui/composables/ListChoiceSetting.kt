package com.toggl.settings.ui.composables

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.toggl.models.domain.SettingsType
import com.toggl.settings.compose.ThemedPreview
import com.toggl.settings.domain.SettingsViewModel

@Composable
internal fun ListChoiceSetting(
    model: SettingsViewModel.ListChoice
) {
    Text(
        text = model.label,
        style = MaterialTheme.typography.body2
    )
    Text(
        text = model.selectedValueTitle,
        style = MaterialTheme.typography.body2
    )
}

internal val previewListChoice = SettingsViewModel.ListChoice("First day of the week", SettingsType.FirstDayOfTheWeek, "Wednesday")

@Preview("ListChoice light theme")
@Composable
fun PreviewListChoiceLight() {
    ThemedPreview {
        SettingsRow {
            ListChoiceSetting(previewListChoice)
        }
    }
}

@Preview("ListChoice dark theme")
@Composable
fun PreviewListChoiceDark() {
    ThemedPreview(darkTheme = true) {
        SettingsRow {
            ListChoiceSetting(previewListChoice)
        }
    }
}