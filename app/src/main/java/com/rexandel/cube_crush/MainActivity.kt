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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        setContent {
            val themeManager = rememberThemeManager()

            CubeCrushTheme(appTheme = themeManager.currentTheme) {
                androidx.compose.material3.Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(themeManager)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(themeManager: com.rexandel.cube_crush.ui.theme.ThemeManager) {
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
                themeManager = themeManager
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