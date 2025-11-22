package com.rexandel.cube_crush.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.rexandel.cube_crush.repository.UserRepository

@Stable
class ThemeManager(private val userRepository: UserRepository) {
    var currentTheme by mutableStateOf<AppTheme>(AppTheme.SYSTEM)
        private set

    init {
        loadSavedTheme()
    }

    private fun loadSavedTheme() {
        val savedTheme = userRepository.getSavedTheme()
        currentTheme = savedTheme
    }

    fun setTheme(theme: AppTheme) {
        currentTheme = theme
        userRepository.saveTheme(theme)
    }
}

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

@Composable
fun rememberThemeManager(): ThemeManager {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    return remember { ThemeManager(userRepository) }
}