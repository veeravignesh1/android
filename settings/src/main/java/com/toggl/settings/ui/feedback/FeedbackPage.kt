package com.toggl.settings.ui.feedback

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.layout.Spacer
import androidx.ui.layout.Stack
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.height
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredSize
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.OutlinedButton
import androidx.ui.material.OutlinedTextField
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.toggl.architecture.Loadable
import com.toggl.settings.R
import com.toggl.common.feature.compose.ThemedPreview
import com.toggl.common.feature.compose.theme.TogglTheme
import com.toggl.common.feature.compose.theme.grid_1
import com.toggl.common.feature.compose.theme.grid_2
import com.toggl.settings.domain.SettingsAction
import kotlinx.coroutines.flow.Flow

@Composable
fun FeedbackPage(
    sendFeedbackRequest: Flow<Loadable<Unit>>,
    statusBarHeight: Dp,
    navigationBarHeight: Dp,
    dispatcher: (SettingsAction) -> Unit = {}
) {
    val sendFeedbackRequestState by sendFeedbackRequest.collectAsState(Loadable.Uninitialized)
    TogglTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.padding(top = statusBarHeight),
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface,
                    title = { Text(text = stringResource(R.string.submit_feedback)) },
                    navigationIcon = {
                        IconButton(onClick = { dispatcher(SettingsAction.FinishedEditingSetting) }) {
                            Icon(Icons.Filled.ArrowBack)
                        }
                    }
                )
            },
            bodyContent = {
                FeedbackForm(
                    sendFeedbackRequestState,
                    navigationBarHeight,
                    dispatcher
                )
            }
        )
    }
}

@Composable
fun FeedbackForm(
    sendFeedbackRequestState: Loadable<Unit>,
    bottomPadding: Dp,
    dispatcher: (SettingsAction) -> Unit = {}
) {
    Column(Modifier.padding(grid_1).fillMaxSize()) {
        Text(text = stringResource(R.string.feedback_note))
        Spacer(modifier = Modifier.height(grid_2))

        var textState by state { TextFieldValue("") }
        Stack(modifier = Modifier.fillMaxWidth().preferredHeight(140.dp)) {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text(text = stringResource(R.string.submit_feedback)) },
                modifier = Modifier.matchParentSize()
            )
            if (sendFeedbackRequestState is Loadable.Loading) {
                CircularProgressIndicator(modifier = Modifier.gravity(Alignment.Center).preferredSize(60.dp, 60.dp))
            }
        }

        Spacer(modifier = Modifier.height(grid_2))
        OutlinedButton(
            onClick = { dispatcher(SettingsAction.SendFeedbackTapped(textState.text)) },
            modifier = Modifier.fillMaxWidth().padding(bottom = bottomPadding),
            enabled = sendFeedbackRequestState is Loadable.Uninitialized && textState.text.isNotBlank()
        ) {
            Text(text = stringResource(R.string.submit_feedback))
        }
    }
}

@Preview("Feedback form light theme")
@Composable
fun PreviewFeedbackFormLight() {
    ThemedPreview(darkTheme = false) {
        FeedbackForm(Loadable.Uninitialized, 10.dp) {}
    }
}

@Preview("Feedback form dark theme")
@Composable
fun PreviewFeedbackFormDark() {
    ThemedPreview(darkTheme = true) {
        FeedbackForm(Loadable.Uninitialized, 10.dp) {}
    }
}
