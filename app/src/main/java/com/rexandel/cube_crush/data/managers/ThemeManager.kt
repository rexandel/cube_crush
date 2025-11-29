package com.rexandel.cube_crush.data.managers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rexandel.cube_crush.domain.managers.AppTheme
import com.rexandel.cube_crush.data.repositories.SettingsRepositoryImpl

@Stable
class ThemeManager(private val settingsRepository: SettingsRepositoryImpl) {
    var currentTheme by mutableStateOf<AppTheme>(AppTheme.SYSTEM)
        private set

    init {
        loadSavedTheme()
    }

    private fun loadSavedTheme() {
        val savedTheme = settingsRepository.getSavedTheme()
        currentTheme = savedTheme
    }

    fun setTheme(theme: AppTheme) {
        currentTheme = theme
        settingsRepository.saveTheme(theme)
    }
}

@Composable
fun rememberThemeManager(settingsRepository: SettingsRepositoryImpl): ThemeManager {
    return remember { ThemeManager(settingsRepository) }
}