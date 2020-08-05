package com.toggl.settings.ui.common

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Dialog
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TextButton
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.toggl.models.domain.SettingsType
import com.toggl.models.domain.User
import com.toggl.models.validation.ApiToken
import com.toggl.models.validation.Email
import com.toggl.settings.R
import com.toggl.settings.compose.ThemedPreview
import com.toggl.settings.compose.theme.Shapes
import com.toggl.settings.compose.theme.TogglTheme
import com.toggl.settings.compose.theme.grid_3
import com.toggl.settings.compose.theme.grid_8
import com.toggl.settings.domain.SettingsAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@Composable
internal fun TextPickerDialogWithHeader(
    setting: Flow<SettingsType.TextSetting>,
    user: Flow<User>,
    dispatcher: (SettingsAction) -> Unit = {}
) {
    Dialog(
        onCloseRequest = { dispatcher(SettingsAction.FinishedEditingSetting) }
    ) {
        TogglTheme {
            Box(shape = Shapes.medium, backgroundColor = MaterialTheme.colors.background) {
                TextPickerContent(setting, user, dispatcher)
            }
        }
    }
}

@Composable
internal fun TextPickerContent(
    settingFlow: Flow<SettingsType.TextSetting>,
    user: Flow<User>,
    dispatcher: (SettingsAction) -> Unit = {}
) {
    val setting by settingFlow.collectAsState(initial = SettingsType.TextSetting.Name   )

    val validateText: (String) -> Boolean = { text ->
        when (setting) {
            SettingsType.TextSetting.Name -> text.isNotBlank()
            SettingsType.TextSetting.Email -> Email.from(text) is Email.Valid
        }
    }

    val initialText by user.map {
        when (setting) {
            SettingsType.TextSetting.Name -> it.name
            SettingsType.TextSetting.Email -> it.email.toString()
        }
    }.collectAsState(initial = "")

    var textState by state { TextFieldValue(initialText) }
    Column(modifier = Modifier.padding(grid_3) + Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(
                when(setting) {
                    SettingsType.TextSetting.Name -> R.string.name
                    SettingsType.TextSetting.Email -> R.string.email
                }
            ),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.preferredHeight(grid_8)
        )
        TextField(
            value = textState,
            onValueChange = { textState = it},
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.gravity(Alignment.End)) {
            TextButton(onClick = { dispatcher(SettingsAction.FinishedEditingSetting) }) {
                Text(text = stringResource(R.string.cancel))
            }
            TextButton(onClick = {
                if (validateText(textState.text)) {
                    dispatcher(SettingsAction.UpdateName(textState.text))
                }
            }) {
                Text(text = stringResource(R.string.ok))
            }

        }
    }
}

private val validUser = User(
    id = 0,
    apiToken = ApiToken.from("12345678901234567890123456789012") as ApiToken.Valid,
    defaultWorkspaceId = 1,
    email = Email.from("valid.mail@toggl.com") as Email.Valid,
    name = "User name"
)

@Preview("Text picker light theme")
@Composable
fun PreviewTextPickerDialogWithHeaderLight() {
    ThemedPreview {
        TextPickerContent(
            flowOf(SettingsType.TextSetting.Email),
            flowOf(validUser)
        ) { }
    }
}

@Preview("Text picker dark theme")
@Composable
fun PreviewTextPickerDialogWithHeaderDark() {
    ThemedPreview(darkTheme = true) {
        TextPickerContent(
            flowOf(SettingsType.TextSetting.Name),
            flowOf(validUser)
        ) { }
    }
}
