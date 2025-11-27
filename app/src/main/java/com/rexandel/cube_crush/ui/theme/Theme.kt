package com.rexandel.cube_crush.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.rexandel.cube_crush.data.managers.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = DarkYellow,
    secondary = Brown,
    tertiary = Black,
    background = Black,
    surface = Black,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,

    primaryContainer = Purple,
    secondaryContainer = Cyan,
    error = Red
)

private val LightColorScheme = lightColorScheme(
    primary = Yellow,
    secondary = LightBlue,
    tertiary = Orange,
    background = White,
    surface = White,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,

    primaryContainer = Pink,
    secondaryContainer = Teal,
    error = Red
)

@Composable
fun CubeCrushTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}