package com.rexandel.cube_crush.domain.repositories

import com.rexandel.cube_crush.domain.managers.AppTheme
import com.rexandel.cube_crush.domain.managers.AppLocale

interface SettingsRepository {
    fun saveTheme(theme: AppTheme)
    fun getSavedTheme(): AppTheme
    fun saveLocale(locale: AppLocale)
    fun getSavedLocale(): AppLocale
}