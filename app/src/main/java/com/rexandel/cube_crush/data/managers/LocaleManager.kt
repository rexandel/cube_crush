package com.rexandel.cube_crush.data.managers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.rexandel.cube_crush.data.repositories.SettingsRepository
import java.util.Locale

@Stable
class LocaleManager(private val settingsRepository: SettingsRepository) {
    var currentLocale by mutableStateOf<AppLocale>(AppLocale.SYSTEM)
        private set

    init {
        loadSavedLocale()
    }

    private fun loadSavedLocale() {
        val savedLocale = settingsRepository.getSavedLocale()
        currentLocale = savedLocale
    }

    fun setLocale(locale: AppLocale) {
        currentLocale = locale
        settingsRepository.saveLocale(locale)
    }

    fun getCurrentLocale(): Locale {
        return when (currentLocale) {
            AppLocale.RUSSIAN -> Locale("ru")
            AppLocale.ENGLISH -> Locale.ENGLISH
            AppLocale.SYSTEM -> Locale.getDefault()
        }
    }
}

enum class AppLocale {
    RUSSIAN, ENGLISH, SYSTEM
}

@Composable
fun rememberLocaleManager(): LocaleManager {
    val context = LocalContext.current
    val settingsRepository = remember { SettingsRepository(context) }
    return remember { LocaleManager(settingsRepository) }
}