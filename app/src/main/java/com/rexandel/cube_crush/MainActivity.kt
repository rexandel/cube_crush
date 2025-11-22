package com.rexandel.cube_crush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.rexandel.cube_crush.repository.UserRepository
import com.rexandel.cube_crush.ui.screens.GameScreen
import com.rexandel.cube_crush.ui.screens.MenuScreen
import com.rexandel.cube_crush.ui.screens.SettingsScreen
import com.rexandel.cube_crush.ui.screens.SplashScreen
import com.rexandel.cube_crush.ui.screens.auth.LoginScreen
import com.rexandel.cube_crush.ui.screens.auth.RegisterScreen
import com.rexandel.cube_crush.ui.theme.CubeCrushTheme
import com.rexandel.cube_crush.ui.theme.rememberThemeManager
import com.rexandel.cube_crush.ui.locale.rememberLocaleManager
import java.util.Locale
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.ui.platform.LocalConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        applySavedLocale()

        setContent {
            val themeManager = rememberThemeManager()
            val localeManager = rememberLocaleManager()

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
                        AppNavigation(themeManager, localeManager)
                    }
                }
            }
        }
    }

    private fun applySavedLocale() {
        val userRepository = UserRepository(this)
        val savedLocale = userRepository.getSavedLocale()

        val locale = when (savedLocale) {
            com.rexandel.cube_crush.ui.locale.AppLocale.RUSSIAN -> Locale("ru")
            com.rexandel.cube_crush.ui.locale.AppLocale.ENGLISH -> Locale.ENGLISH
            else -> Locale.getDefault()
        }

        updateAppLocale(locale)
    }

    private fun updateAppLocale(localeManager: com.rexandel.cube_crush.ui.locale.LocaleManager) {
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
    themeManager: com.rexandel.cube_crush.ui.theme.ThemeManager,
    localeManager: com.rexandel.cube_crush.ui.locale.LocaleManager
) {
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }

    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

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
                onNavigateToRegister = { currentScreen = AppScreen.Register }
            )
            AppScreen.Register -> RegisterScreen(
                onRegisterSuccess = { currentScreen = AppScreen.Menu },
                onBackToLogin = { currentScreen = AppScreen.Login }
            )
            AppScreen.Menu -> MenuScreen(
                onStartGame = { currentScreen = AppScreen.Game },
                onSettings = { currentScreen = AppScreen.Settings },
                onExit = {
                    (context as? android.app.Activity)?.finish()
                }
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
                localeManager = localeManager
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