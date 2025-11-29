package com.rexandel.cube_crush

import android.os.Bundle
import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import com.rexandel.cube_crush.data.repositories.UserRepositoryImpl
import com.rexandel.cube_crush.data.repositories.ScoreRepositoryImpl
import com.rexandel.cube_crush.data.repositories.SettingsRepositoryImpl
import com.rexandel.cube_crush.domain.managers.AppLocale
import com.rexandel.cube_crush.data.managers.LocaleManager
import com.rexandel.cube_crush.data.managers.ThemeManager
import com.rexandel.cube_crush.data.managers.rememberLocaleManager
import com.rexandel.cube_crush.data.managers.rememberThemeManager
import com.rexandel.cube_crush.ui.screens.GameScreen
import com.rexandel.cube_crush.ui.screens.MenuScreen
import com.rexandel.cube_crush.ui.screens.SettingsScreen
import com.rexandel.cube_crush.ui.screens.SplashScreen
import com.rexandel.cube_crush.ui.screens.auth.LoginScreen
import com.rexandel.cube_crush.ui.screens.auth.RegisterScreen
import com.rexandel.cube_crush.ui.theme.CubeCrushTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var scoreRepository: ScoreRepositoryImpl
    private lateinit var settingsRepository: SettingsRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        userRepository = UserRepositoryImpl.getInstance(this)
        scoreRepository = ScoreRepositoryImpl.getInstance(this)
        settingsRepository = SettingsRepositoryImpl.getInstance(this)

        applySavedLocale()

        setContent {
            val themeManager = rememberThemeManager(settingsRepository)
            val localeManager = rememberLocaleManager(settingsRepository)

            val currentLocale = localeManager.getCurrentLocale()
            val configuration = LocalConfiguration.current

            LaunchedEffect(currentLocale, configuration) {
                updateAppLocale(localeManager)
            }

            CompositionLocalProvider(
                LocalContext provides LocalContext.current
            ) {
                CubeCrushTheme(appTheme = themeManager.currentTheme) {
                    androidx.compose.material3.Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(themeManager, localeManager, userRepository, scoreRepository)
                    }
                }
            }
        }
    }

    private fun applySavedLocale() {
        val savedLocale = settingsRepository.getSavedLocale()

        val locale = when (savedLocale) {
            AppLocale.RUSSIAN -> Locale("ru")
            AppLocale.ENGLISH -> Locale.ENGLISH
            else -> Locale.getDefault()
        }

        updateAppLocale(locale)
    }

    private fun updateAppLocale(localeManager: LocaleManager) {
        val locale = localeManager.getCurrentLocale()
        updateAppLocale(locale)
    }

    private fun updateAppLocale(locale: Locale) {
        val resources: Resources = resources
        val configuration: Configuration = resources.configuration

        if (configuration.locale != locale) {
            configuration.setLocale(locale)
            @Suppress("DEPRECATION")
            resources.updateConfiguration(configuration, resources.displayMetrics)

            val context = createConfigurationContext(configuration)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}

@Composable
fun AppNavigation(
    themeManager: ThemeManager,
    localeManager: LocaleManager,
    userRepository: UserRepositoryImpl,
    scoreRepository: ScoreRepositoryImpl
) {
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        showSplash = false

        val currentUser = userRepository.getCurrentUser()
        currentScreen = if (currentUser != null) {
            AppScreen.Menu
        } else {
            AppScreen.Login
        }
    }

    if (showSplash) {
        SplashScreen()
    } else {
        when (currentScreen) {
            AppScreen.Splash -> SplashScreen()
            AppScreen.Login -> LoginScreen(
                onLoginSuccess = { currentScreen = AppScreen.Menu },
                onNavigateToRegister = { currentScreen = AppScreen.Register },
                userRepository = userRepository
            )
            AppScreen.Register -> RegisterScreen(
                onRegisterSuccess = { currentScreen = AppScreen.Menu },
                onBackToLogin = { currentScreen = AppScreen.Login },
                userRepository = userRepository
            )
            AppScreen.Menu -> MenuScreen(
                onStartGame = { currentScreen = AppScreen.Game },
                onSettings = { currentScreen = AppScreen.Settings }
            )
            AppScreen.Game -> GameScreen(
                onExitToMenu = { currentScreen = AppScreen.Menu }
            )
            AppScreen.Settings -> SettingsScreen(
                onBackToMenu = { currentScreen = AppScreen.Menu },
                onLogout = {
                    currentScreen = AppScreen.Login
                },
                themeManager = themeManager,
                localeManager = localeManager,
                userRepository = userRepository,
                scoreRepository = scoreRepository
            )
        }
    }
}

sealed class AppScreen {
    object Splash : AppScreen()
    object Login : AppScreen()
    object Register : AppScreen()
    object Menu : AppScreen()
    object Game : AppScreen()
    object Settings : AppScreen()
}