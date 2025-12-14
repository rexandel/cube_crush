package com.rexandel.cube_crush.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.rexandel.cube_crush.domain.managers.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    secondary = LightBlue,
    tertiary = DarkYellow,
    background = Black,
    surface = Gray,
    onPrimary = White,
    onSecondary = Black,
    onBackground = White,
    onSurface = White,

    primaryContainer = LightBlue,
    secondaryContainer = Cyan,
    error = Red
)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = LightBlue,
    tertiary = Yellow,
    background = OffWhite,
    surface = White,
    onPrimary = White,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,

    primaryContainer = LightBlue,
    secondaryContainer = Cyan,
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