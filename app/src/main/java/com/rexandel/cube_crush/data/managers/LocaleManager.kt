package com.rexandel.cube_crush.data.managers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rexandel.cube_crush.data.repositories.SettingsRepositoryImpl
import com.rexandel.cube_crush.domain.managers.AppLocale
import java.util.Locale

@Stable
class LocaleManager(private val settingsRepository: SettingsRepositoryImpl) {
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

@Composable
fun rememberLocaleManager(settingsRepository: SettingsRepositoryImpl): LocaleManager {
    return remember { LocaleManager(settingsRepository) }
}