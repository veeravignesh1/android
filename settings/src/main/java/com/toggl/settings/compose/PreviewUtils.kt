package com.toggl.settings.compose

import androidx.compose.Composable
import androidx.ui.material.Surface
import com.toggl.settings.compose.theme.TogglTheme

@Composable
internal fun ThemedPreview(
    darkTheme: Boolean = false,
    children: @Composable() () -> Unit
) {
    TogglTheme(darkTheme = darkTheme) {
        Surface {
            children()
        }
    }
}
