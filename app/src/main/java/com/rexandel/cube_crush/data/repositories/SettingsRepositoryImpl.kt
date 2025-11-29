package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.rexandel.cube_crush.domain.managers.AppLocale
import com.rexandel.cube_crush.domain.managers.AppTheme
import com.rexandel.cube_crush.domain.repositories.SettingsRepository

class SettingsRepositoryImpl private constructor(context: Context) : SettingsRepository {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: SettingsRepositoryImpl? = null

        fun getInstance(context: Context): SettingsRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun saveTheme(theme: AppTheme) {
        sharedPref.edit().putString("app_theme", theme.name).apply()
    }

    override fun getSavedTheme(): AppTheme {
        val themeName = sharedPref.getString("app_theme", AppTheme.SYSTEM.name)
        return try {
            AppTheme.valueOf(themeName ?: AppTheme.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            AppTheme.SYSTEM
        }
    }

    override fun saveLocale(locale: AppLocale) {
        sharedPref.edit().putString("app_locale", locale.name).apply()
    }

    override fun getSavedLocale(): AppLocale {
        val localeName = sharedPref.getString("app_locale", AppLocale.SYSTEM.name)
        return try {
            AppLocale.valueOf(localeName ?: AppLocale.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            AppLocale.SYSTEM
        }
    }
}