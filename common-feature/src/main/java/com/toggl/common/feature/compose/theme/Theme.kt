package com.toggl.common.feature.compose.theme

import androidx.compose.Composable
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.material.MaterialTheme
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette

private val LightThemeColors = lightColorPalette(
    primary = Pink,
    primaryVariant = PinkDark,
    onPrimary = PurpleDeep,
    secondary = Yellow,
    secondaryVariant = Yellow,
    onSecondary = PurpleDeep,
    surface = WhiteAlpine,
    background = White,
    error = RedDark
)

private val DarkThemeColors = darkColorPalette(
    primary = Pink,
    primaryVariant = PinkDark,
    onPrimary = White,
    secondary = Yellow,
    onSecondary = PurpleDeep,
    surface = PurpleDeep,
    background = PurpleDeep,
    error = RedLight
)

@Composable
fun TogglTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = ThemeTypography,
        shapes = Shapes,
        content = content
    )
}
