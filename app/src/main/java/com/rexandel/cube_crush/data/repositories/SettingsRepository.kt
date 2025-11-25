package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.rexandel.cube_crush.data.managers.AppLocale
import com.rexandel.cube_crush.data.managers.AppTheme

class SettingsRepository(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveTheme(theme: AppTheme) {
        sharedPref.edit().putString("app_theme", theme.name).apply()
    }

    fun getSavedTheme(): AppTheme {
        val themeName = sharedPref.getString("app_theme", AppTheme.SYSTEM.name)
        return try {
            AppTheme.valueOf(themeName ?: AppTheme.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            AppTheme.SYSTEM
        }
    }

    fun saveLocale(locale: AppLocale) {
        sharedPref.edit().putString("app_locale", locale.name).apply()
    }

    fun getSavedLocale(): AppLocale {
        val localeName = sharedPref.getString("app_locale", AppLocale.SYSTEM.name)
        return try {
            AppLocale.valueOf(localeName ?: AppLocale.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            AppLocale.SYSTEM
        }
    }
}