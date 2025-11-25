package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.rexandel.cube_crush.data.managers.AppLocale
import com.rexandel.cube_crush.data.managers.AppTheme

class SettingsRepository private constructor(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

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