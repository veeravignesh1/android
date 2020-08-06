package com.toggl.common.feature.compose

import androidx.compose.Composable
import androidx.ui.material.Surface
import com.toggl.common.feature.compose.theme.TogglTheme

@Composable
fun ThemedPreview(
    darkTheme: Boolean = false,
    children: @Composable () -> Unit
) {
    TogglTheme(darkTheme = darkTheme) {
        Surface {
            children()
        }
    }
}
