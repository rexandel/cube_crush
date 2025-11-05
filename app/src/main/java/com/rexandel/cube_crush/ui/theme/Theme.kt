package com.rexandel.cube_crush.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Yellow,
    secondary = DarkYellow,
    tertiary = Brown
)

private val LightColorScheme = lightColorScheme(
    primary = Yellow,
    secondary = DarkYellow,
    tertiary = Brown
)

@Composable
fun CubeCrushTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = CustomTypography,
        content = content
    )
}